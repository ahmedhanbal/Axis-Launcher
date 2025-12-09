package com.example.axis

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IconPacksActivity : AppCompatActivity() {
    private lateinit var toolbar: Toolbar
    private lateinit var iconPackRecycler: RecyclerView
    private val packs = mutableListOf<IconPack>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_packs)
        toolbar = findViewById(R.id.iconPacksToolbar)
        iconPackRecycler = findViewById(R.id.iconPacksRecycler)
        setupToolbar()
        seedData()
        setupRecycler()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun seedData() {
        packs.clear()
        packs.add(IconPack("classic", "Classic", R.drawable.ic_launcher_foreground))
        packs.add(IconPack("flat", "Flat", R.drawable.ic_launcher_foreground))
        packs.add(IconPack("outline", "Outline", R.drawable.ic_launcher_foreground))
    }

    private fun setupRecycler() {
        val preferenceManager = com.example.axis.utils.PreferenceManager(this)
        iconPackRecycler.layoutManager = GridLayoutManager(this, 3)
        iconPackRecycler.adapter = IconPacksAdapter(packs) { pack ->
            preferenceManager.iconPack = pack.id
            android.widget.Toast.makeText(this, "Applied ${pack.name} Icon Pack", android.widget.Toast.LENGTH_SHORT).show()
            // In a real app, we would reload icons here
        }
    }
}

data class IconPack(val id: String, val name: String, val previewRes: Int)

class IconPacksAdapter(
    private val packs: List<IconPack>,
    private val onClick: (IconPack) -> Unit
) : RecyclerView.Adapter<IconPacksAdapter.PackViewHolder>() {
    inner class PackViewHolder(val view: android.view.View) : RecyclerView.ViewHolder(view) {
        val name: android.widget.TextView = view.findViewById(R.id.itemIconPackName)
        val preview: android.widget.ImageView = view.findViewById(R.id.itemIconPackPreview)
    }

    override fun onCreateViewHolder(parent: android.view.ViewGroup, viewType: Int): PackViewHolder {
        val v = android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_icon_pack, parent, false)
        return PackViewHolder(v)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val pack = packs[position]
        holder.name.text = pack.name
        holder.preview.setImageResource(pack.previewRes)
        holder.view.setOnClickListener { onClick(pack) }
    }

    override fun getItemCount(): Int = packs.size
}
