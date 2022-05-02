package minesweeper

import kotlin.random.Random
import kotlin.text.split


fun main() {

    println("How many mines do you want on the field?")
    val numOfMines = readln().toInt()
    makeFields()
    drawField(Field.mineField)
    println("Set/unset mines marks or claim a cell as free: ")
    val input = readln().split(" ")
    val initX = input[1].toInt() + 1
    val initY = input[0].toInt() + 1
    val freeOrMine = input[2]

    installMines(numOfMines, initX, initY, freeOrMine)
    putNumbers()
    //openEmptiness(initX, initY)
    play(initX, initY, freeOrMine)

}

fun play(x: Int, y: Int, freeOrMine: String) {
    if ("mine" == freeOrMine) {                                 // MINE
        if (Field.mineFieldView[x][y] == '.') {
            Field.mineFieldView[x][y] = '*'
            if (!checkWin()) {
                drawField(Field.mineFieldView)
                println("Set/unset mines marks or claim a cell as free: ")
                val input = readln().split(" ")
                val initX = input[1].toInt() + 1
                val initY = input[0].toInt() + 1
                val freeOrMine = input[2]
                play(initX, initY, freeOrMine)
            }
        } else if (Field.mineFieldView[x][y] == '*') {
            Field.mineFieldView[x][y] = '.'
            if (!checkWin()) {
                drawField(Field.mineFieldView)
                println("Set/unset mines marks or claim a cell as free: ")
                val input = readln().split(" ")
                val initX = input[1].toInt() + 1
                val initY = input[0].toInt() + 1
                val freeOrMine = input[2]
                play(initX, initY, freeOrMine)
            }
        } else {
            println("AAAARRRRGGGGHHHHH!!!!!!!!")
        }
    } else {                                                    // FREE
        if ('X' == Field.mineField[x][y]) {                      // if X -> GAME OVER
            drawField(Field.mineField)
            print("You stepped on a mine and failed!")
        } else if ('.' == Field.mineField[x][y]) {              // if empty -> open and play further
            Field.mineField[x][y] = '/'
            Field.mineFieldView[x][y] = '/'
            openEmptiness(x, y)
            if (!checkWin()) {
                drawField(Field.mineFieldView)
                println("Set/unset mines marks or claim a cell as free: ")
                val input = readln().split(" ")
                val initX = input[1].toInt() + 1
                val initY = input[0].toInt() + 1
                val freeOrMine = input[2]
                play(initX, initY, freeOrMine)
            }
        } else if (Field.mineField[x][y] in '1'..'8') {              // if number -> open and play further
//            Field.mineField[x][y] = '/'
            Field.mineFieldView[x][y] = Field.mineField[x][y]
            openEmptiness(x, y)
            if (!checkWin()) {
                drawField(Field.mineFieldView)
                println("Set/unset mines marks or claim a cell as free: ")
                val input = readln().split(" ")
                val initX = input[1].toInt() + 1
                val initY = input[0].toInt() + 1
                val freeOrMine = input[2]
                play(initX, initY, freeOrMine)
            }
        } else {
            drawField(Field.mineFieldView)
            println("Set/unset mines marks or claim a cell as free: ")
            val input = readln().split(" ")
            val initX = input[1].toInt() + 1
            val initY = input[0].toInt() + 1
            val freeOrMine = input[2]
            play(initX, initY, freeOrMine)
        }
    }
}

fun checkWin(): Boolean {
    for (i in 2..10) {
        for (j in 2..10) {
            if((Field.mineField[i][j] == 'X' && Field.mineFieldView[i][j] != '*') || (Field.mineField[i][j] != 'X' && Field.mineFieldView[i][j] == '*')) {
                return false
            }
        }
    }
    drawField(Field.mineField)
    print("Congratulations! You found all the mines!")
    return true
}

fun openEmptiness(x: Int, y: Int) {
    for (i in x - 1..x + 1) {
        for (j in y - 1..y + 1) {
            if (i == x && j == y) {
                continue
            } else {
                if ('.' == Field.mineField[i][j]) {
                    Field.mineField[i][j] = '/'
                    Field.mineFieldView[i][j] = '/'
                    openEmptiness(i, j)
                } else if (Field.mineField[i][j] in '1'..'8') {
                    Field.mineFieldView[i][j] = Field.mineField[i][j]
                }
            }
        }
    }
}

fun installMines(numOfMines: Int, initX: Int, initY: Int, freeOrMine: String) {
    var installedMines = 0
    if ("mine" == freeOrMine) {
        Field.mineField[initY][initY] = 'X'
        installedMines = 1
    }
    while (installedMines < numOfMines) {
        installedMines = 0
        val vert = Random.nextInt(2, 11)
        val hor = Random.nextInt(2, 11)
        if (vert == initX && hor == initY) {
            continue
        } else {
            Field.mineField[vert][hor] = 'X'
            for (x in 2..10) {
                for (y in 2..10) {
                    installedMines = if (Field.mineField[x][y] == 'X') installedMines + 1 else installedMines
                }
            }
        }
    }
}

//-----------------------------------//-----------------------------------//

fun drawField(list: MutableList<MutableList<Char>>) {
    println()
    for (i in list.indices) {
        println(list[i].joinToString(""))
    }
}

fun makeFields() {
    for (i in 0 until 9) {
        for (j in 0 until 9) {
            Field.mineField[i].add('.')
            Field.mineFieldView[i].add('.')
        }
    }
    Field.mineField.add(0, mutableListOf(' ', '|', '1', '2', '3', '4', '5', '6', '7', '8', '9', '|'))
    Field.mineField.add(1, mutableListOf('—', '|', '—', '—', '—', '—', '—', '—', '—', '—', '—', '|'))
    Field.mineField.add(mutableListOf('—', '|', '—', '—', '—', '—', '—', '—', '—', '—', '—', '|'))
    val i: Int
    for (i in 2..10) {
        Field.mineField[i].add('|')
        Field.mineField[i].add(0, '/' + i)
        Field.mineField[i].add(1, '|')
    }

    Field.mineFieldView.add(0, mutableListOf(' ', '|', '1', '2', '3', '4', '5', '6', '7', '8', '9', '|'))
    Field.mineFieldView.add(1, mutableListOf('—', '|', '—', '—', '—', '—', '—', '—', '—', '—', '—', '|'))
    Field.mineFieldView.add(mutableListOf('—', '|', '—', '—', '—', '—', '—', '—', '—', '—', '—', '|'))
    for (i in 2..10) {
        Field.mineFieldView[i].add('|')
        Field.mineFieldView[i].add(0, '/' + i)
        Field.mineFieldView[i].add(1, '|')
    }
}

fun putNumbers() {
    for (i in 2..10) {
        for (j in 2..10) {
            if (Field.mineField[i][j] != 'X') {
                if (i == 2) {               // 1
                    if (j == 2) {           // 1.1
                        var counter = 0
                        counter = if (Field.mineField[2][3] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][2] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][3] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    } else if (j == 10) {    // 1.2
                        var counter = 0
                        counter = if (Field.mineField[2][9] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][9] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][10] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    } else {                // 1.3
                        var counter = 0
                        counter = if (Field.mineField[2][j - 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[2][j + 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][j - 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][j] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[3][j + 1] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    }
                } else if (i == 10) {         // 2
                    if (j == 2) {           // 2.1
                        var counter = 0
                        counter = if (Field.mineField[9][2] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][3] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[10][3] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    } else if (j == 10) {    // 2.2
                        var counter = 0
                        counter = if (Field.mineField[10][9] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][9] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][10] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    } else {                // 2.3
                        var counter = 0
                        counter = if (Field.mineField[10][j - 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[10][j + 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][j - 1] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][j] == 'X') counter + 1 else counter
                        counter = if (Field.mineField[9][j + 1] == 'X') counter + 1 else counter
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                        Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    }
                } else if (j == 2) {          // 3
                    var counter = 0
                    counter = if (Field.mineField[i - 1][j] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i + 1][j] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i - 1][j + 1] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i][j + 1] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i + 1][j + 1] == 'X') counter + 1 else counter
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                } else if (j == 10) {          // 4
                    var counter = 0
                    counter = if (Field.mineField[i - 1][j] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i + 1][j] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i - 1][j - 1] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i][j - 1] == 'X') counter + 1 else counter
                    counter = if (Field.mineField[i + 1][j - 1] == 'X') counter + 1 else counter
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                } else {                      // 5
                    var counter = 0
                    for (x in i - 1..i + 1) {
                        for (y in j - 1..j + 1) {
                            counter = if (Field.mineField[x][y] == 'X') counter + 1 else counter
                        }
                    }
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                    Field.mineField[i][j] = if (counter == 0) Field.mineField[i][j] else counter.toChar() + 48
                }
            }
        }
    }
}



/*
    fun play() {
        println("Set/delete mines marks (x and y coordinates): ")
        val coords = readln().split(" ")
        val i = coords[1].toInt() + 1
        val j = coords[0].toInt() + 1
        if (Field.mineField[i][j] in '1'..'9') {
            println("There is a number here!")
            play()
        } else if (Field.mineField[i][j] == 'X' && Field.mineFieldView[i][j] == '.') {
            Field.mineFieldView[i][j] = '*'
            if (checkWin()) {
                println("Congratulations! You found all the mines!")
            } else {
                for (l in 0 until 12) {
                    println(Field.mineFieldView[l].joinToString(""))
                }
                play()
            }
        } else if (Field.mineField[i][j] == 'X' && Field.mineFieldView[i][j] == '*') {
            Field.mineFieldView[i][j] = '.'
            if (checkWin()) {
                println("Congratulations! You found all the mines!")
            } else {
                for (l in 0 until 12) {
                    println(Field.mineFieldView[l].joinToString(""))
                }
                play()
            }
        } else if (Field.mineField[i][j] == '.' && Field.mineFieldView[i][j] == '*') {
            Field.mineFieldView[i][j] = '.'
            if (checkWin()) {
                println("Congratulations! You found all the mines!")
            } else {
                for (l in 0 until 12) {
                    println(Field.mineFieldView[l].joinToString(""))
                }
                play()
            }
        } else if (Field.mineField[i][j] == '.' && Field.mineFieldView[i][j] == '.') {
            Field.mineFieldView[i][j] = '*'
            if (checkWin()) {
                println("Congratulations! You found all the mines!")
            } else {
                for (l in 0 until 12) {
                    println(Field.mineFieldView[l].joinToString(""))
                }
                play()
            }
        }

    }
    play()
}*/






