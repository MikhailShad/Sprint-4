package ru.sber.generics

// 1.
fun <K, V> compare(p1: Pair<K, V>, p2: Pair<K, V>): Boolean {
    return p1.first == p2.first
            && p1.second == p2.second
}

// 2.
fun <T : Comparable<T>> countGreaterThan(anArray: Array<T>, elem: T): Int {
    return anArray.asSequence()
        .filter { it > elem }
        .count()
}

// 3.
class Sorter<T : Comparable<T>> {
    val list: MutableList<T> = mutableListOf()

    fun add(value: T) {
        for (i in 0..list.size) {
            if (i == list.size || value < list[i]) {
                list.add(i, value)
                return
            }
        }
    }
}

// 4.
class Stack<T> {
    val list: MutableList<T> = mutableListOf()

    fun push(value: T) = list.add(0, value)

    fun pop() = list.removeAt(0)

    fun isEmpty(): Boolean = list.isEmpty()
}