open class Expenses(val AmountExpense: Int, val Category: String, val Data: Int){

    fun Display() {
        println("Сумма расхода: $AmountExpense, Категория: $Category, Дата: $Data")
    }
}