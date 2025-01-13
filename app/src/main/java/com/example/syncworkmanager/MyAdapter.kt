import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.syncworkmanager.R
import com.example.syncworkmanager.User

class MyAdapter(private var itemList: MutableList<User>) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder class holds item views
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id = view.findViewById<TextView>(R.id.idTextView)
        val email = view.findViewById<TextView>(R.id.emailTextView)
        val password = view.findViewById<TextView>(R.id.passwordTextView)
        val name = view.findViewById<TextView>(R.id.nameTextView)
        val role = view.findViewById<TextView>(R.id.roleTextView)
        val creationDate = view.findViewById<TextView>(R.id.creationDateTextView)
        val avatar = view.findViewById<ImageView>(R.id.avatarImageView)
        val updateDate = view.findViewById<TextView>(R.id.updatedDateTextView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        // Bind the data to the views
        holder.id.text = "ID: ${item.id}"
        holder.email.text = item.email
        holder.password.text = "Password: ${item.password}"
        holder.name.text = item.name
        holder.role.text = "Role: ${item.role}"
        holder.creationDate.text = "Created: ${item.creationAt}"
        holder.updateDate.text = "Updated: ${item.updatedAt}"

        // Use a library like Glide or Picasso to load the avatar
        Glide.with(holder.avatar.context)
            .load(item.avatar)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.avatar)

    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun updateData(user:User) {
        itemList.add(user)
        notifyDataSetChanged()
    }

}
