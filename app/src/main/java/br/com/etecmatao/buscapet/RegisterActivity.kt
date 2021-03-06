package br.com.etecmatao.buscapet

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.etecmatao.buscapet.model.User
import br.com.etecmatao.buscapet.model.UserPasswordCredential
import br.com.etecmatao.buscapet.service.UserService
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
    }

    fun register(v: View) {
        val fields = arrayOf(txtFirstName, txtLastName, txtEmail, txtPassword, txtConfirmPassword)

        var anyEmpty = fields.map { validEmpty(it) }.filter { !it }.any()

        if (anyEmpty || !validMatch(txtConfirmPassword, txtPassword)) {
            return
        }

        val user = User(
            firstName = txtFirstName.text.toString(),
            lastName = txtLastName.text.toString(),
            email = txtEmail.text.toString(),
            picture = ""
        )

        val credential = UserPasswordCredential(
            email = txtEmail.text.toString(),
            password = txtPassword.text.toString()
        )

        UserService.instance.save(user, credential){
            Toast.makeText(
                this,
                getString(R.string.msg_user_created),
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }



    }

    private fun validEmpty(field: TextInputEditText): Boolean {
        field.error = null

        if (TextUtils.isEmpty(field.text.toString())) {
            field.error = getString(R.string.msg_required_field)
            return false
        }

        return true
    }

    private fun validMatch(field: TextInputEditText, anotherField: TextInputEditText): Boolean {
        if (validEmpty(field) && validEmpty(anotherField)) {
            val value = field.text.toString()
            val anotherValue = anotherField.text.toString()

            if (value != anotherValue) {
                field.error = getString(R.string.msg_password_does_not_match)
                anotherField.error = getString(R.string.msg_password_does_not_match)

                return false
            }

            return true
        }

        return false
    }
}
