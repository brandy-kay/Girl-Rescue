package com.adhanjadevelopers.girl_rescue.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.adhanjadevelopers.girl_rescue.R
import com.adhanjadevelopers.girl_rescue.databinding.ActivitySignInBinding
import com.adhanjadevelopers.girl_rescue.models.User
import com.adhanjadevelopers.girl_rescue.ui.fragments.ForgotFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "SignInActivity"

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.buttonSignIn.setOnClickListener {

            binding.progressBarSignIn.isVisible = true

            when {
                binding.editTextEmailSignIn.editText?.text.toString().isNullOrEmpty() -> {
                    binding.editTextEmailSignIn.editText?.error = "Required Email"
                    binding.progressBarSignIn.isVisible = false
                    return@setOnClickListener
                }
                binding.editTextTextPasswordSignIn.editText?.text.toString().isNullOrEmpty() -> {
                    binding.editTextTextPasswordSignIn.error = "Required Phone Number"
                    binding.progressBarSignIn.isVisible = false
                    return@setOnClickListener
                }
                else -> {
                    val email = binding.editTextEmailSignIn.editText?.text.toString()
                    val password = binding.editTextTextPasswordSignIn.editText?.text.toString()


                    firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                if (firebaseAuth.currentUser!!.isEmailVerified){
                                    binding.progressBarSignIn.isVisible = false
                                    startActivity(Intent(this, MainActivity::class.java))
                                    Toast.makeText(this, "logged in", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                else{
                                   binding.progressBarSignIn.isVisible = false
                                    Toast.makeText(this, "Verify your email first", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Toast.makeText(this, "${it.exception}", Toast.LENGTH_SHORT).show()
                                binding.progressBarSignIn.isVisible = false
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_SHORT)
                                .show()
                            binding.progressBarSignIn.isVisible = false
                        }

                }
            }
        }
        binding.textViewDontHaveAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.textViewForgotPassword.setOnClickListener {
            ForgotFragment().show(supportFragmentManager,"forgotFragment")
        }
    }
}