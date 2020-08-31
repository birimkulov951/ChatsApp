package com.example.chatsapp.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.R
import com.example.chatsapp.adapters.UserAdapter
import com.example.chatsapp.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*

/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment() {

    private val TAG = "SearchFragment"

    private var userAdapter: UserAdapter? = null
    private var mUsersList: List<Users>? = null
    private var recyclerView: RecyclerView? = null
    private var searchEditText: EditText? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_search, container, false)

        searchEditText = view.findViewById(R.id.search_user_edit_text)
        recyclerView = view.findViewById(R.id.recycler_view_search_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)


        mUsersList = ArrayList()
        retrieveAllUsers()

        searchEditText!!.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchForUsers(p0.toString().toLowerCase())
            }

        })


        return view
    }

    private fun retrieveAllUsers() {
        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        var refUsers = FirebaseDatabase.getInstance().reference.child("Users")
        refUsers.addValueEventListener(object: ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                (mUsersList as ArrayList<Users>).clear()
                if (searchEditText!!.text.toString() == "") {

                    for (snapshot in p0.children) {
                        var user: Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID()).equals(firebaseUserId)) {

                            (mUsersList as ArrayList<Users>).add(user)

                        }
                    }
                    userAdapter = UserAdapter(context!!, mUsersList!!, false)
                    recyclerView!!.adapter = userAdapter

                }
            }
        })
    }

    private fun searchForUsers(str: String) {
        var firebaseUserId = FirebaseAuth.getInstance().currentUser!!.uid
        var queryUsers = FirebaseDatabase.getInstance().reference
            .child("Users").orderByChild("search")
            .startAt(str)
            .endAt(str + "\uf8ff" )

        queryUsers.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                (mUsersList as ArrayList<Users>).clear()
                for (snapshot in p0.children) {
                    var user: Users? = snapshot.getValue(Users::class.java)
                    if (!(user!!.getUID()).equals(firebaseUserId)) {

                        (mUsersList as ArrayList<Users>).add(user)

                    }
                }
                userAdapter = UserAdapter(context!!,mUsersList!!,false)
                recyclerView!!.adapter = userAdapter
            }

        })


    }

}