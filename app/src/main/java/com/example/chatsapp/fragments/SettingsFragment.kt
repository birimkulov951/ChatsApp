package com.example.chatsapp.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.chatsapp.R
import com.example.chatsapp.model.Users
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : Fragment() {

    var userReference: DatabaseReference? = null
    var firebaseUser: FirebaseUser? = null

    public val Request_Code = 34
    private var imageUri: Uri? = null
    private var storageRef:StorageReference? = null
    private var coverChecker: String? = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_settings, container, false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef = FirebaseStorage.getInstance().reference.child("User Images")

        userReference!!.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val user: Users? = p0.getValue(Users::class.java)
                    if (context != null) {
                        view.username_settings.text = user!!.getUsername()
                        Picasso.get().load(user.getProfile()).into(view.profile_image_settings)
                        Picasso.get().load(user.getCover()).into(view.cover_image_settings)
                    }
                }
            }

        })

        view.profile_image_settings.setOnClickListener{
            pickImage()
        }

        view.cover_image_settings.setOnClickListener{
            coverChecker = "cover"
            pickImage()
        }

        view.instagram_settings.setOnClickListener{
            setInstagramLink()
        }

        return view
    }

    private fun setInstagramLink() {
        var builder: AlertDialog.Builder = AlertDialog.Builder(context!!, R.style.Theme_AppCompat_DayNight_Dialog_Alert)
        builder.setTitle("Write your username:")

        val editText = EditText(context)
        editText.hint = "e.g. jhonny-official"

        builder.setView(editText)
        builder.setPositiveButton("Create",DialogInterface.OnClickListener { dialog, which ->
            val str = editText.text.toString()

            if (str == "") {
                Toast.makeText(context,"Please write something...",Toast.LENGTH_SHORT).show()
            } else {
                saveInstagramLink(str)
            }
        })
        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener { dialog, which ->
            dialog.cancel()
        })
        builder.show()
    }

    private fun saveInstagramLink(str: String) {
        val instagramMap = HashMap<String,Any>()
        instagramMap["instagram"] = "https://m.instagram.com/$str"
        userReference!!.updateChildren(instagramMap).addOnCompleteListener{task->
            if (task.isSuccessful) {
                Toast.makeText(context,"Updated successfully",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun pickImage() {
        var intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,Request_Code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Request_Code && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            Toast.makeText(context,"Uploading...",Toast.LENGTH_SHORT).show()
            uploadImageToDatabase()
        }
    }

    private fun uploadImageToDatabase() {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("image is uploading...")
        progressBar.show()

        if (imageUri != null) {
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString() + ".jpg")

            var uploadTask: StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWith(Continuation <UploadTask.TaskSnapshot, Task<Uri>>{ task ->
                if (!task.isSuccessful) {
                    task.exception?.let{
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val downloadUrl = task.result
                    val url = downloadUrl.toString()

                    if (coverChecker == "cover") {
                        val mapCoverIMG = HashMap<String,Any>()
                        mapCoverIMG["cover"] = url
                        userReference!!.updateChildren(mapCoverIMG)
                        coverChecker = ""
                    } else {
                        val mapProfileIMG = HashMap<String,Any>()
                        mapProfileIMG["profile"] = url
                        userReference!!.updateChildren(mapProfileIMG)
                    }
                    progressBar.dismiss()
                }
            }
        }
    }
}