import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.midterm.ui.model.MenuItem

class CartViewModel : ViewModel() {
    // List of items added to the cart
    val cartItems = mutableStateListOf<MenuItem>()

    fun addToCart(item: MenuItem) {
        cartItems.add(item)
    }

    fun removeFromCart(item: MenuItem) {
        cartItems.remove(item)
    }

    fun getTotalPrice(): Double {
        return cartItems.sumOf { it.price }
    }
    fun clearCart() {
        cartItems.clear()
    }
}