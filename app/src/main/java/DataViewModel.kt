import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject

class DataViewModel: ViewModel() {
    private var _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    var changing: Boolean = false
    val formData: LiveData<JSONObject>
        get() = _formData

    fun addField(key: String, value: Any): DataViewModel {
        _formData.value?.put(key, value); return this
    }

    fun setObject(obj: JSONObject) {
        _formData.value = obj
    }

    fun clearFields() {
        _formData = MutableLiveData<JSONObject>().apply { value = JSONObject() }
    }
}