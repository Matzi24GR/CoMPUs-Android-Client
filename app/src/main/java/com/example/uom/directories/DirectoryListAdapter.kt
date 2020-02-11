package com.example.uom.directories

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.R

class DirectoryListAdapter internal constructor(val context: Context, val array: ArrayList<ItemClass>,val parentUrl: String): RecyclerView.Adapter<DirectoryListAdapter.DirectoryViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class DirectoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.item_name_text_view)
        var parent: LinearLayout = itemView.findViewById(R.id.directory_parent)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val itemView = inflater.inflate(R.layout.directory_item, parent, false)
        return DirectoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {

        val current = array[position]
        holder.nameTextView.text = current.name

        holder.parent.setOnClickListener{
                if (current.isDirectory){
                    Log.i("Directory","Clicked")
                    val bundle = bundleOf(
                            "parent" to parentUrl,
                            "URL" to current.url
                    )
                    it.findNavController().navigate(R.id.action_directoryFragment_self,bundle)
                } else if(current.imgsrc.contains("pdf.gif")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = Uri.parse(current.url)
                    intent.setDataAndType(uri,"application/pdf")

                    context.startActivity(intent)
//                    val bundle = bundleOf("url" to current.url)
//                    it.findNavController().navigate(R.id.action_open_pdf,bundle)
                }
        }
    }

    override fun getItemCount() = array.size
}