package tech.groceryradar.groceryradar

class Group(
    val QrCode: String = "",
    var listName: String = "",
    var people: MutableList<String> = mutableListOf(),
    var items: MutableList<Items> = mutableListOf(),
    var customItems: MutableList<Items> = mutableListOf(),
    var tags: MutableList<Tag> = mutableListOf(),
)

class Items(
    val name: String = "",
    val owner: String = "",
    val quantity: Int = 1,
    val status: Boolean = false,
    val tag: Tag = Tag()
)

class Tag(
    val color: String = "",
    val tagName: String = ""
)