package org.wit.waterfordgalleryguide.views.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import org.wit.waterfordgalleryguide.activities.ButtonActivity
import org.wit.waterfordgalleryguide.main.MainApp
import org.wit.waterfordgalleryguide.models.AllGalleriesFireStore
import org.wit.waterfordgalleryguide.models.GalleryFireStore

class LoginPresenter (val view: LoginView)  {
    private lateinit var loginIntentLauncher : ActivityResultLauncher<Intent>
    var app: MainApp = view.application as MainApp
    var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var fireStore: GalleryFireStore? = null
    var allFireStore: AllGalleriesFireStore? = null

    init{
        registerLoginCallback()
        if (app.galleries is GalleryFireStore) {
            fireStore = app.galleries as GalleryFireStore
        }
        if (app.allGalleries is AllGalleriesFireStore) {
            allFireStore = app.allGalleries as AllGalleriesFireStore
        }
    }


    fun doLogin(email: String, password: String) {
        view.showProgress()
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                if (fireStore != null) {
                    fireStore!!.fetchGalleries{
                        view?.hideProgress()
                        val launcherIntent = Intent(view, ButtonActivity::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                } else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, ButtonActivity::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
                if (allFireStore != null ) {
                    allFireStore!!.fetchAllGalleries {
                        val launcherIntent = Intent(view, ButtonActivity::class.java)
                        loginIntentLauncher.launch(launcherIntent)
                    }
                }
                else {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, ButtonActivity::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view?.hideProgress()
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }

    }

    fun doSignUp(email: String, password: String) {
        view.showProgress()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view) { task ->
            if (task.isSuccessful) {
                fireStore!!.fetchGalleries {
                    view?.hideProgress()
                    val launcherIntent = Intent(view, ButtonActivity::class.java)
                    loginIntentLauncher.launch(launcherIntent)
                }
            } else {
                view.showSnackBar("Login failed: ${task.exception?.message}")
            }
            view.hideProgress()
        }
    }

    private fun registerLoginCallback(){
        loginIntentLauncher =
            view.registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }
}