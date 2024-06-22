import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData

class HomeFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences.getInstance(application.dataStore)

    val userData = userPreferences.getUserData().asLiveData()
}
