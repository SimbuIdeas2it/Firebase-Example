package com.example.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasetest.databinding.ActivityMainBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

data class User(val id: String,
                val username: String,
                val email: String,
                val password: String)


class MainActivity: AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)
        val database = FirebaseDatabase.getInstance()
        var authentication = FirebaseAuth.getInstance()

        binding.signbtn.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_LONG).show()

            val userName = binding.nametxt.text.toString()
            val email = binding.emailtxt.text.toString()
            val password = binding.passwordtxt.text.toString()
            authentication.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {
                    authentication.currentUser?.updateProfile(
                        UserProfileChangeRequest
                            .Builder()
                            .setDisplayName(userName)
                            .build())

                } else {

                }
            }
        }

        binding.addrecord.setOnClickListener {
            val newUser = database.reference.child("user").push()
            val id = newUser.key
            val user = User(id,"Test", "test@gmail.com", "12345")
            database.reference.child("user").child(id).setValue(user)
        }

        binding.signout.setOnClickListener {
            authentication.signOut()
        }

        binding.signIn.setOnClickListener {
            val email = binding.emailtxt.text.toString()
            val password = binding.passwordtxt.text.toString()
            authentication.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isComplete && it.isSuccessful) {

                }
            }
        }

        binding.getAll.setOnClickListener {
            database.reference
                .child("user")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError?) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot?) {
                        snapshot?.run {
                            val user1 = children.mapNotNull { it.getValue(User::class.java) }
                            print(user1)
                        }
                    }
                })
        }
    }
}