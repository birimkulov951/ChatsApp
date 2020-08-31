package com.example.chatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolBar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolBar)
        supportActionBar!!.title = "Register"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener{
            val intent = Intent(this@RegisterActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        mAuth = FirebaseAuth.getInstance()

        button_register.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser() {
        val userName: String = user_name_register.text.toString()
        val email: String = email_register.text.toString()
        val password: String = password_register.text.toString()

        if (userName == "") {
            Toast.makeText(this,"Please write user name", Toast.LENGTH_SHORT).show()
        } else if (email == "") {
            Toast.makeText(this,"Please write email", Toast.LENGTH_SHORT).show()
        } else if (password == "") {
            Toast.makeText(this,"Please write password", Toast.LENGTH_SHORT).show()
        } else {

            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{

                if (it.isSuccessful) {
                    firebaseUserId = mAuth.currentUser!!.uid
                    refUsers = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

                    val userHashMap = HashMap<String, Any>()
                    userHashMap["uid"] = firebaseUserId
                    userHashMap["username"] = userName
                    userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/simple-chat-8a728.appspot.com/o/blank_profile.png?alt=media&token=c05f396a-0431-4c0a-9949-53ca44753a8c"
                    userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/simple-chat-8a728.appspot.com/o/white_background.png?alt=media&token=b2df5a65-5f71-4634-a62b-37c8396ef621"
                    userHashMap["status"] = "offline"
                    userHashMap["search"] = userName.toLowerCase()
                    userHashMap["instagram"] = "https://m.instagram.com"

                    refUsers.updateChildren(userHashMap)
                        .addOnCompleteListener{
                            if (it.isSuccessful) {
                                val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }
                        }

                } else {
                    Toast.makeText(this,"Error message: " + it.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }

    }


    //todo add backpress
    override fun onBackPressed() {
        super.onBackPressed()
    }
}