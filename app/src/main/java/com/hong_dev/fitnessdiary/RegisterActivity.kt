package com.hong_dev.fitnessdiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.hong_dev.fitnessdiary.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.etId.text.toString()
            val password = binding.etPw.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty()){
                registerUser(email, password)
            }

        }

    }

    private fun registerUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if(task.isSuccessful){
                    // 회원가입 성공
                    auth.currentUser?.sendEmailVerification()?.addOnCompleteListener{sendTask ->
                        if(sendTask.isSuccessful){
                            // 이메일 전송 성공
                            Toast.makeText(this, "인증 메일이 전송되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent: Intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else{
                            // 이메일 전송 실패
                            Toast.makeText(this, "인증 메일 전송에 실패하였습니다.", Toast.LENGTH_SHORT).show()

                        }
                    }
                }else{
                    // 회원가입 실패
                    Toast.makeText(this, "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}