using System.Collections;
using System.Collections.Generic;

namespace HashTableOrder
{
    public class LinkedHashTable<TKey, TValue> : ILinkedHashTable<TKey, TValue>, 
                                                 IEnumerable<KeyValueEntry<TKey, TValue>>
    {
        private const double MaxLoadFactor = 0.5;
        private readonly System.Func<TKey, TKey, bool> _comparer;
        private KeyValueEntry<TKey, TValue>[] _entries;
        private bool[] _isDeleted;
        private bool _resizing;
        
        
        public LinkedHashTable(int capacity = 32)
        {
            if (capacity > 0)
            {
                Capacity = CeilPowerOf2(capacity);
                _entries = new KeyValueEntry<TKey, TValue>[Capacity];
                _isDeleted = new bool[Capacity];
                _head = null;
                _tail = null;

                if (typeof(System.IEquatable<TKey>).IsAssignableFrom(typeof(TKey)))
                {
                    _comparer = (l, r) => ((System.IEquatable<TKey>)l).Equals(r);
                }
                else
                {
                    _comparer = (l, r) => l.Equals(r);
                }
            }
            else
            {
                throw new System.ArgumentException("Positive number required.");
            }
        }

        public bool Contains(TKey key)
        {
            TryGet(key, out bool success);
            return success;
        }

        public TValue Get(TKey key)
        {
            var returnValue = TryGet(key, out bool success);
            if (success)
            {
                return returnValue;
            }

            throw new System.ArgumentException(
                "Key should be in the table, use Get(TKey, out bool) instead.");
        }

        public TValue TryGet(TKey key, out bool success)
        {
            int iteration = 0;
            int keyHashCode = key.GetHashCode();
            int hashIndex = GetIndex(keyHashCode, iteration++);
            success = false;
            var returnValue = default(TValue);
            
            while (_isDeleted[hashIndex] || _entries[hashIndex] != null)
            {
                if (_entries[hashIndex] != null)
                {
                    if (_comparer(_entries[hashIndex].Key, key))
                    {
                        success = true;
                        return _entries[hashIndex].Value;
                    }
                }
                
                hashIndex = GetIndex(keyHashCode, iteration++);
            }

            return returnValue;
        }
        
        public void Put(TKey key, TValue value)
        {
            if (key == null) throw new System.ArgumentNullException();
            int iteration = 0;
            int keyHashCode = key.GetHashCode();
            int hashIndex = GetIndex(keyHashCode, iteration++);

            while (!_isDeleted[hashIndex] &&
                    _entries[hashIndex] != null &&
                   !_comparer(_entries[hashIndex].Key, key))
            {
                hashIndex = GetIndex(keyHashCode, iteration++);
            }

            if (_isDeleted[hashIndex] || _entries[hashIndex] == null)
            {
                Size++;
            }

            // Adding and raising OnElementAdded

            if (_entries[hashIndex] == null)
            {
                _entries[hashIndex] = new KeyValueEntry<TKey, TValue>(key, value);
                AddEntryToList(_entries[hashIndex]);
            }
            else
            {
                _entries[hashIndex].Value = value;
            }

            var args = new ElementAddedEventArgs<KeyValueEntry<TKey, TValue>>
            {
                CollectionName = this.GetType().Name,
                ChangedOnResize = _resizing,
                Data = _entries[hashIndex]
            };
            OnElementAdded(args);

            if (Size > Capacity * MaxLoadFactor)
            {
                Resize(Capacity * 2);
            }
        }

        public TValue Remove(TKey key)
        {
            int iteration = 0;
            int keyHashCode = key.GetHashCode();
            int hashIndex = GetIndex(keyHashCode, iteration++);
            var returnValue = default(TValue);

            while (_isDeleted[hashIndex] || _entries[hashIndex] != null)
            {
                if (_entries[hashIndex] != null)
                {
                    if (_comparer(_entries[hashIndex].Key, key))
                    {
                        Size--;
                        returnValue = _entries[hashIndex].Value;
                        RemoveEntryFromList(_entries[hashIndex]);
                        _isDeleted[hashIndex] = true;
                        
                        var args = new ElementRemovedEventArgs<KeyValueEntry<TKey, TValue>>
                        {
                            CollectionName = this.GetType().Name,
                            Data = _entries[hashIndex]
                        };
                        _entries[hashIndex] = null;
                        OnElementDeleted(args);
                        
                        if (Size < Capacity * MaxLoadFactor * MaxLoadFactor)
                        {
                            Resize(Capacity / 2);
                        }

                        return returnValue;
                    }
                }

                hashIndex = GetIndex(keyHashCode, iteration++);
            }

            return returnValue;
        }

        public TValue this[TKey key]
        {
            get => Get(key);
            set => Put(key, value);
        }

        public int Size
        {
            get;
            private set;
        }

        public int Capacity
        {
            get;
            private set;
        }

        public TKey[] Keys
        {
            get
            {
                if (_head == null || Size == 0) return new TKey[0];
                var keysArray = new TKey[Size];
                int index = 0;
                
                for (var tmp = _head; tmp != null; tmp = tmp.Next)
                {
                    keysArray[index++] = tmp.Key;
                }

                return keysArray;
            }
        }

        private bool Resize(int newSize)
        {
            if (newSize < Size) return false;
            
            var oldEntries = new KeyValueEntry<TKey, TValue>[Size];
            var keys = Keys;
            for (int i = 0; i < keys.Length; ++i)
            {
                oldEntries[i] = new KeyValueEntry<TKey, TValue>(keys[i], Get(keys[i]));
            }

            Capacity = CeilPowerOf2(newSize);
            Size = 0;
            _isDeleted = new bool[Capacity];
            _entries = new KeyValueEntry<TKey, TValue>[Capacity];
            _head = null;
            _tail = null;
            
            _resizing = true;
            foreach (var entry in oldEntries)
            {
                Put(entry.Key, entry.Value);
            }
            _resizing = false;
            
            return true;
        }

        IEnumerator IEnumerable.GetEnumerator()
        {
            return GetEnumerator();
        }

        public IEnumerator<KeyValueEntry<TKey, TValue>> GetEnumerator()
        {
            return new EntriesEnumerator(_head);
        }

        private class EntriesEnumerator : IEnumerator<KeyValueEntry<TKey, TValue>>
        {
            private KeyValueEntry<TKey, TValue> _current;
            private readonly KeyValueEntry<TKey, TValue> _initialPosition;

            public EntriesEnumerator(KeyValueEntry<TKey, TValue> entry)
            {
                _current = new KeyValueEntry<TKey, TValue>(default, default)
                {
                    Next = entry
                };
                _initialPosition = _current;
            }

            public bool MoveNext()
            {
                if (_current?.Next == null) return false;

                _current = _current.Next;
                return true;
            }

            public void Reset()
            {
                _current = _initialPosition;
            }

            object IEnumerator.Current => Current;

            public void Dispose()
            {
            }

            public KeyValueEntry<TKey, TValue> Current
            {
                get
                {
                    if (_current == null) throw new System.InvalidOperationException();
                    return _current;
                }
            }
        }

//==========Helper Functions==========
        private static int CeilPowerOf2(int num)
        {
            if (num < 0)
                throw new System.ArgumentException("Positive number required.");
            int power = 1;
            while (power < num)
                power *= 2;
            return power;
        }
        
        private int GetIndex(int key, int iteration)
        {
            // if key is negative
            key = key & 0x7fffffff;
            // same as (_ % Capacity) because of capacity being 2^n
            int hash1 = key & (Capacity - 1);
            // no real reason for choosing odd number
            int oddNumber = (Capacity / 4) | 1;
            // hash2 is odd, therefore we can iterate over all _table entries 
            int hash2 = (oddNumber - (key % oddNumber)) | 1;
            return (hash1 + iteration * hash2) & (Capacity - 1);
        }   
    }
}