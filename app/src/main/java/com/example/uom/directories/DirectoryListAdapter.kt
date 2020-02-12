package com.example.uom.directories

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.R

class DirectoryListAdapter internal constructor(val context: Context, val array: ArrayList<ItemClass>): RecyclerView.Adapter<DirectoryListAdapter.DirectoryViewHolder>(){
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    inner class DirectoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameTextView: TextView = itemView.findViewById(R.id.item_name_text_view)
        var parent: ConstraintLayout = itemView.findViewById(R.id.directory_parent)
        var icon: ImageView = itemView.findViewById(R.id.icon_image_view)
        var subTextView: TextView = itemView.findViewById(R.id.item_subtitle_text_view)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DirectoryViewHolder {
        val itemView = inflater.inflate(R.layout.directory_item, parent, false)
        return DirectoryViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DirectoryViewHolder, position: Int) {

        val current = array[position]
        holder.nameTextView.text = current.name
        holder.subTextView.text = current.subtitle

        if(current.imgsrc.contains("pdf.gif")){
            //Icon made by Dimitry Miroliubov from www.flaticon.com
            holder.icon.setImageResource(R.drawable.ic_pdf)
        } else if(current.imgsrc.contains("ppt.gif")){
            //Icon made by Pixel perfect from www.flaticon.com
            holder.icon.setImageResource(R.drawable.ic_powerpoint)
        } else if(current.imgsrc.contains("js.gif")){
            //Icon made by Pixelmeetup from www.flaticon.com
            holder.icon.setImageResource(R.drawable.ic_source_code)
        } else if(current.imgsrc.contains("doc1.gif")){
            //Icon made by Freepik from www.flaticon.com
            holder.icon.setImageResource(R.drawable.ic_docs)
        } else if(current.isDirectory) {
            holder.icon.setImageResource(R.drawable.ic_folder_black_24dp)
        } else {
            //Icon made by Freepik from www.flaticon.com
            holder.icon.setImageResource(R.drawable.ic_paper)
        }

        holder.parent.setOnClickListener{
                if (current.isDirectory){
                    Log.i("Directory","Clicked")
                    val bundle = bundleOf(
                            "parent" to current.name,
                            "URL" to current.url
                    )
                    it.findNavController().navigate(R.id.action_directoryFragment_self,bundle)
                } else if(current.imgsrc.contains("pdf.gif")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    val uri = Uri.parse(current.url)
                    intent.setDataAndType(uri,"application/pdf")

                    context.startActivity(intent)
                } else if(current.imgsrc.contains("js.gif")){
                    val bundle = bundleOf("url" to current.url)
                    it.findNavController().navigate(R.id.action_open_web,bundle)
                }
        }
    }

    override fun getItemCount() = array.size
}