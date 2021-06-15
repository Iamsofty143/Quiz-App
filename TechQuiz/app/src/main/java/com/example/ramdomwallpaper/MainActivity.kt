package com.example.ramdomwallpaper

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RestrictTo
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.ramdomwallpaper.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    var questionText: String? = null  // variable to store QuestionText
    var TotalQuestion= 0               // variable to count Total Questions
    var TotalScore = 0                 // variable to count score
    var randomNo=3                    // Random NO varible initially given value 3 , we can give it anything
    var ans: String? = null           // variable to store correct Answer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main) // made this comment as data binding is done

        binding= DataBindingUtil.setContentView(this, R.layout.activity_main)   // data binding code
        LoadQuiz()  // LoadAPI function is called

    }

    fun rand(start: Int, end: Int): Int {                     // we are using this function to get random No. , we will use this no to set option randomlly
        require(start <= end) { "Illegal Argument" }        // this function take 2 arrguments of int type
        return (start..end).random()
    }

        fun LoadQuiz(){  // LoadApi function defination , this function will call api

           val url = "https://opentdb.com/api.php?amount=1&category=18&type=multiple"  // API link

// Request a string response from the provided URL.
           val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,   // volley standard request code
               Response.Listener { response ->

                   var   StoreJsonArray = response.getJSONArray("results") // storing jsonarray 'result' in  variable StoreJsonArray "result"



                  for (i in 0 until StoreJsonArray.length()){    // we have json array so this loop is used to trace array elements
                      var responseObject = StoreJsonArray.getJSONObject(i)   // storing elements in responseObject variable
                      questionText  = responseObject.getString("question")  //fetching out jsonObject from json array result "question" is key
                      ans = responseObject.getString("correct_answer")    // fetching jsonObject 'correct_ans" from json array "correct_answer' is key
                      var option = responseObject.getString("incorrect_answers")

                      val delim ="," // this is used to divide String into parts "," from , string will get divide
                    var  list = option.split(delim) // storing divided string into list variable

                      for (i in 1..1)  randomNo = rand(1,4)    // calling rand function for random No.
                      binding.questionText.setText(questionText) // this code disply question in questionText


                      if(randomNo==1){  // if random no is 1 then Correct ans will store in option1

                          binding.op1.setText("\"$ans \" ")
                          binding.op2.setText(list[0].drop(1)) // .drop(1) is used to delete First charter of String
                          binding.op3.setText(list[1])
                          binding.op4.setText(list[2].dropLast(1)) //dropLast(1) is used to drop last 1 charter of string *
                      }

                      if(randomNo==2){  //if random no is 2 then correct ans will store in option2

                          binding.op2.setText("\"$ans \" ")
                          binding.op1.setText(list[0].drop(1)) //
                          binding.op4.setText(list[1])
                          binding.op3.setText(list[2].dropLast(1))

                      }
                      if(randomNo==3){ // ans will store in option 3

                          binding.op3.setText("\"$ans \" ")
                          binding.op4.setText(list[0].drop(1)) //
                          binding.op2.setText(list[1])
                          binding.op1.setText(list[2].dropLast(1))
                      }

                     if(randomNo==4) { // ans at option 4

                         binding.op4.setText("\"$ans \" ")
                         binding.op3.setText(list[0].drop(1)) //
                         binding.op1.setText(list[1])
                         binding.op2.setText(list[2].dropLast(1)) //
                     }

                  }

                   binding.pic.setImageResource(R.drawable.qmark) // this code to display question mark image

                   binding.point.setText("Score is $TotalScore out of $TotalQuestion Question") // code to display score
                   TotalQuestion=TotalQuestion+1 // to count on No. of question loaded



               },
               Response.ErrorListener {
                   Toast.makeText(this, "something went wrong", Toast.LENGTH_LONG).show()  // if API does not get call then this will show s msg in toast form
               })

// Add the request to the RequestQueue.
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)

        }

    fun checkOption4(view: View) {  // fun if option 4 is clicked

        if(randomNo==4) {                                                // because ans is stored according to random No.
            binding.pic.setImageResource(R.drawable.right)               // code to display correct image sign
            TotalScore = TotalScore + 1                                  // code to add 1 point in score as ans is correct
        }else{                                                            // random is not = 4 that means correct ans is not at option 4 so
            binding.pic.setImageResource(R.drawable.wrong)                 // code to display Incorrect image sign
            binding.questionText.setText("Wrong Answer \n Option $ans")    // code will Display correct answer in questionText
            binding.op4.setText("Correct Answer is $ans")                  // code will Display correct answer in option 4 also *no required code*
        }

         Thread.sleep(1000)   // this code is used to hold app in same position for 1000 mili sec
        LoadQuiz()      // api called again

    }

    fun checkOption3(view: View) { // function if option three is clicked
        if(randomNo==3) {
            binding.pic.setImageResource(R.drawable.right)
            TotalScore = TotalScore + 1
        }else{
            binding.pic.setImageResource(R.drawable.wrong)
            binding.questionText.setText("Wrong Answer \n Option $ans")
            binding.op3.setText("Correct Answer is $ans")


        }
        Thread.sleep(1000)

        LoadQuiz()

    }
    fun checkOption1(view: View) {
        if(randomNo==1) {
            binding.pic.setImageResource(R.drawable.right)
            TotalScore = TotalScore + 1
        }else{
            binding.pic.setImageResource(R.drawable.wrong)
            binding.questionText.setText("Wrong Answer \n Option $ans")
            binding.op1.setText("Correct Answer is $ans")
        }
        Thread.sleep(1000)
        LoadQuiz()

    }
    fun checkOptioin2(view: View) {
        if(randomNo==2) {
            binding.pic.setImageResource(R.drawable.right)
            TotalScore = TotalScore + 1
        }else{
            binding.pic.setImageResource(R.drawable.wrong)
            binding.questionText.setText("Wrong Answer \n Option $ans")
            binding.op2.setText("Correct Answer is $ans")
        }
        Thread.sleep(1000)
        LoadQuiz()

    }

    fun btfun(view: View) {                                                     // Function if button (Load Again) button is clicked
        LoadQuiz()
    }

}
//Its an simple Android app which fetch Random Tech Question and Options through Triva Question Api. Its is simple but Knowledge enhancing App. I have made this app for Learning purpose.