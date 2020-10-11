package tech.groceryradar.groceryradar

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_group_dialog.view.*

class MainActivity : AppCompatActivity() {
    @SuppressLint("LogNotTimber")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val groups = mutableListOf<Group>()

        groupRecyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = GroupRecyclerAdapter(groups)
        groupRecyclerView.adapter = adapter

        //loads the groups that the user is in
        val QrCodesList = mutableListOf<String>("QR Code") //TODO read QR code from file
        for (code in QrCodesList) {
            val db = FirebaseFirestore.getInstance()
            val dbRef = db.collection("groups").document(code)

            dbRef.get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    var itemsList: MutableList<Items> = mutableListOf()
                    var customItemsList: MutableList<Items> = mutableListOf()
                    var tagsList: MutableList<Tag> = mutableListOf()

                    dbRef.collection("items").get().addOnSuccessListener {
                        if (!it.isEmpty)
                            itemsList = it.toObjects(Items::class.java)
                    }

                    dbRef.collection("customItems").get().addOnSuccessListener {
                        if (!it.isEmpty)
                            customItemsList = it.toObjects(Items::class.java)
                    }

                    dbRef.collection("tags").get().addOnSuccessListener {
                        if (!it.isEmpty)
                            tagsList = it.toObjects(Tag::class.java)
                    }

                    val peopleList: MutableList<String> =
                        snapshot.get("people") as MutableList<String>
                    groups.add(
                        Group(
                            code,
                            snapshot.getString("listName")!!,
                            peopleList,
                            itemsList,
                            customItemsList,
                            tagsList
                        )
                    )
                    adapter.notifyDataSetChanged()
                }
            }

        }

        joinListBtn.setOnClickListener {

        }

        createListBtn.setOnClickListener {
            val builder = MaterialAlertDialogBuilder(this)
            val inflater = layoutInflater
            val alertLayout = inflater.inflate(R.layout.new_group_dialog, null)
            builder.setView(alertLayout)

            builder
                .setPositiveButton("Create", null)
                .setNegativeButton(
                    "Cancel"
                ) { dialogInterface: DialogInterface, _: Int -> }

            val alert = builder.create()

            //the positiveButton is defined here so I can check if the fields are filled in without closing the Dialog
            alert.setOnShowListener {
                val positiveButton = alert.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    val groupName: String

                    if (alertLayout.GroupTextView.text.toString().isNotEmpty()) {
                        groupName = alertLayout.GroupTextView.text.toString()
                    } else {
                        alertLayout.GroupTextView.error = "Fill out a name"
                        return@setOnClickListener
                    }

                    //TODO generate a QR code and add it to the text file
                    //TODO store the person's name and add it here
                    val group = Group("Code", groupName, mutableListOf("Bader Alrifai"))
                    groups.add(group)
                    adapter.notifyDataSetChanged()

                    val db = FirebaseFirestore.getInstance()
                    val dbRef = db.collection("groups").document("Code")

                    //TODO Add the QR code and person properly here
                    val groupHashMap = hashMapOf(
                        "qrCode" to "Code1",
                        "listName" to groupName,
                        "people" to mutableListOf("Bader Alrifai")
                    )
                    dbRef.set(groupHashMap)

                    alert.dismiss()
                }
            }

            alert.show()

            //removes the weird button BG and changes text color
            val negativeButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            negativeButton.setBackgroundResource(0)
            negativeButton.setTextColor(Color.parseColor("#F44336"))
            val positiveButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            positiveButton.setBackgroundResource(0)
            positiveButton.setTextColor(Color.parseColor("#00e676"))
        }

    }
}