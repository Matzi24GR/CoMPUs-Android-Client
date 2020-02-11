package com.example.uom.directories


import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uom.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.jsoup.Connection
import org.jsoup.Jsoup

/**
 * A simple [Fragment] subclass.
 */
class DirectoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_directory, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.directoryRecyclerView)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val backButton = view.findViewById<Button>(R.id.back_button)

        val cookie = context!!.getSharedPreferences("CREDENTIALS", Context.MODE_PRIVATE).getString("cookie2", null)
        val parent = arguments!!.getString("parent")
        val url = arguments!!.getString("URL")

        val array: ArrayList<ItemClass> = arrayListOf()
        val adapter = DirectoryListAdapter(this@DirectoryFragment.context!!, array,url!!)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this@DirectoryFragment.context)



        Log.i("Directory","Entered Fragment")
        progressBar.visibility = View.VISIBLE
        if (url!!.contains("index.php")) {
            GlobalScope.launch(Dispatchers.Main) {
                val document = GlobalScope.async(Dispatchers.IO) {
                    Jsoup.connect(url)
                            .cookie("PHPSESSID", cookie)
                            .method(Connection.Method.GET)
                            .execute()
                    val response = Jsoup.connect("http://compus.uom.gr/modules/documents/documents.php")
                            .cookie("PHPSESSID", cookie)
                            .method(Connection.Method.GET)
                            .execute()

                    return@async response.parse()
                }.await()

                val itemElements = document.select("tr[onMouseover]")
                for (i in itemElements.indices) {
                    array.add(ItemClass(itemElements[i]))
                    Log.i("Directories", array[i].name +" | "+array[i].url+" | "+array[i].isDirectory)
                }
                progressBar.visibility = View.INVISIBLE
                adapter.notifyDataSetChanged()
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                val document = GlobalScope.async(Dispatchers.IO) {
                    val response = Jsoup.connect(url)
                            .cookie("PHPSESSID", cookie)
                            .method(Connection.Method.GET)
                            .execute()
                    return@async response.parse()
                }.await()

                val itemElements = document.select("tr[onMouseover]")
                for (i in itemElements.indices) {
                    array.add(ItemClass(itemElements[i]))
                    Log.i("Directories", array[i].name +" | "+array[i].url+" | "+array[i].isDirectory)
                }
                progressBar.visibility = View.INVISIBLE
                adapter.notifyDataSetChanged()
            }
        }

        backButton.setOnClickListener{
            val bundle = bundleOf(
                    "parent" to parent,
                    "URL" to parent
            )
            it.findNavController().navigate(R.id.action_directoryFragment_self,bundle)
        }

        return view

    }
}

