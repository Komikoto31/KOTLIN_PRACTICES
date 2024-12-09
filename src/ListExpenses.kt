class ListExpenses: Expenses(1000, "Транспорт", 30) {
    val ListExp = mutableListOf<Expenses>()

    fun Addendum(expense: Expenses){
        ListExp.add(expense)
    }

    fun AllExpenses(){
        for (expense in ListExp){
            expense.Display()
        }
    }


    fun SumExpenses() {
        val ExpensesByCategory = ListExp.groupBy { it.Category }
        if (ExpensesByCategory.isEmpty()){
            println("Нет расходов")
        }
        else {
            println("Сумма расходов по категориям: ")
            for ((Category, expenses) in ExpensesByCategory) {
                val total = expenses.sumOf { it.AmountExpense }
                println("$Category: $total")
            }
        }

    }
}