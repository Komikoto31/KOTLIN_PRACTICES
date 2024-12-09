//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
fun main() {

    val listExpenses = ListExpenses()

    Expenses(1000, "Развлечения", 10).Display()

    // Добавляем несколько расходов
    listExpenses.Addendum(Expenses(1000, "Еда", 9))
    listExpenses.Addendum(Expenses(500, "Транспорт", 20))
    listExpenses.Addendum(Expenses(300, "Еда", 23))
    listExpenses.Addendum(Expenses(200, "Развлечения", 25))
    listExpenses.Addendum(Expenses(1500, "Транспорт", 30))

    // Отображаем все расходы
    println("Все расходы:")
    listExpenses.AllExpenses()

    // Суммируем расходы по категориям
    println(listExpenses.SumExpenses())

}