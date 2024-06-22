import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ardev.myapplication.data.response.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val USER_ID_KEY = stringPreferencesKey("user_id")
    private val NAME_KEY = stringPreferencesKey("username")
    private val TOKEN_KEY = stringPreferencesKey("token")

    fun getUserData(): Flow<LoginResult?> {
        return dataStore.data.map { preferences ->
            val userId = preferences[USER_ID_KEY] ?: return@map null
            val name = preferences[NAME_KEY] ?: return@map null
            val token = preferences[TOKEN_KEY] ?: return@map null
            LoginResult(userId, name, token)
        }
    }

    suspend fun saveUserData(loginResult: LoginResult) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = loginResult.userId
            preferences[NAME_KEY] = loginResult.name
            preferences[TOKEN_KEY] = loginResult.token
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun clearUserData() {
        dataStore.edit { preferences ->
            preferences.remove(USER_ID_KEY)
            preferences.remove(NAME_KEY)
            preferences.remove(TOKEN_KEY)
            preferences.clear()
        }
    }
}