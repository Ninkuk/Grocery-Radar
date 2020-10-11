package tech.groceryradar.groceryradar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.group_cell.view.*

class GroupRecyclerAdapter(private var groups: List<Group>) : RecyclerView.Adapter<GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.group_cell, parent, false)
        return GroupViewHolder(view)
    }

    override fun getItemCount(): Int = groups.size

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.view.listName.text =groups[position].listName
    }

}

class GroupViewHolder(val view: View) : RecyclerView.ViewHolder(view)