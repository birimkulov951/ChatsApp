package com.example.chatsapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatsapp.R
import com.example.chatsapp.model.Users
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.user_search_item_layout.view.*

class UserAdapter(mContext: Context,
                  mUsersList: List<Users>,
                  isChatChecked: Boolean
) : RecyclerView.Adapter<UserAdapter.ViewHolder?>() {

    private val mContext: Context
    private val mUsersList: List<Users>
    private val isChatChecked: Boolean

    init {
        this.mContext = mContext
        this.mUsersList = mUsersList
        this.isChatChecked = isChatChecked
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.user_search_item_layout, parent, false)
        return UserAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mUsersList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var user: Users = mUsersList[position]
        holder.userNameText.text = user!!.getUsername()
        Picasso.get().load(user.getProfile()).placeholder(R.drawable.blank_profile).into(holder.profileImageView)
    }



    //**********************************************************************************************

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {

        var userNameText: TextView
        var profileImageView: CircleImageView
        var onlineImageView: CircleImageView
        var offlineImageView: CircleImageView
        var lastMessageText: TextView

        init {
            userNameText = itemView.findViewById(R.id.user_name_search)
            profileImageView = itemView.findViewById(R.id.profile_image_search)
            onlineImageView = itemView.findViewById(R.id.online_image_search)
            offlineImageView = itemView.findViewById(R.id.offline_image_search)
            lastMessageText = itemView.findViewById(R.id.last_message_search    )

        }
    }


}