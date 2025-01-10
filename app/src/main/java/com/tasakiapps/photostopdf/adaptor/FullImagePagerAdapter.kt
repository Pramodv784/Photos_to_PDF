import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tasakiapps.photostopdf.R
import com.tasakiapps.photostopdf.model.GridViewItem

class FullImagePagerAdapter(
    private val context: Context,
    private val images: List<GridViewItem> // List of image URLs or file paths
) : RecyclerView.Adapter<FullImagePagerAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.full_image_view, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imagePath = images[position]
        Glide.with(context)
            .load(imagePath.path) // Load the image using Glide or another library
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = images.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}
