package com.example.firebasechat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebasechat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.signUpBtn.setOnClickListener {
            val userName = binding.usernameEt.text.toString()
            val email = binding.emailEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val confirmPassword = binding.confirmPasswordEt.text.toString()
            if (userName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                if (password == confirmPassword) {
                    registerUser(userName, email, password)
                }
                else Toast.makeText(this, "Passwords don't match, please check and try again.", Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }

        binding.signInBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

    }


    private fun registerUser(userName: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        var user: FirebaseUser? = auth.currentUser
                        var uid = user!!.uid

                        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                        var hashMap: HashMap<String, String> = HashMap()
                        hashMap.put("uid", uid)
                        hashMap.put("user_name", userName)
                        hashMap.put("profile_image", "")

                        dbReference.setValue(hashMap).addOnCompleteListener(this) {
                            if (it.isSuccessful) {
                                // open home activity
                                Toast.makeText(this, "You successfully registered!", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                }
    }


}