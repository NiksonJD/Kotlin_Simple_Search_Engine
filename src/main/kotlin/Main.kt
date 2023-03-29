package search

import java.io.File

val ii = mutableMapOf<String, MutableList<Int>>()
fun input(prompt: String) = println(prompt).run { readln() }

fun search(listPeople: List<String>) {
    val strategy = input("Select a matching strategy: ALL, ANY, NONE")
    val list = input("\nEnter a name or email to search all suitable people.").split(" ").map { it.lowercase() }
    val filteredMap = ii.filter { entry -> entry.key.lowercase() in list }
    val indices = filteredMap.values.flatten().groupBy { it }.let {
        if (strategy == "ALL") it.filterValues { value -> value.size == list.size } else it
    }.keys.toList()
    val op = { i: Int, s: List<Int> -> if (strategy == "NONE") i !in s else i in s }
    listPeople.filterIndexed { i, _ -> op(i, indices) }.takeIf { it.isNotEmpty() }?.also {
        println("${it.size} persons found:"); it.forEach(::println)
    } ?: println("No matching people found.")
}

fun main(args: Array<String>) {
    val listPeople = (args.getOrNull(1)?.let { File(it) } ?: File("names.txt")).readLines()
    listPeople.forEachIndexed { i, s -> s.split(" ").forEach { ii.getOrPut(it) { mutableListOf() }.add(i) } }
    while (true) {
        when (input("\n=== Menu ===\n1. Find a person\n2. Print all people\n0. Exit")) {
            "0" -> { println("Bye!"); break }
            "2" -> println("\n=== List of people ===").also { listPeople.forEach(::println) }
            "1" -> search(listPeople)
            else -> println("Incorrect option! Try again.")
        }
    }
}