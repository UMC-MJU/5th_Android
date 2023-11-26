package com.example.flo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.flo.databinding.ItemAlbumBinding
import com.example.flo.databinding.ItemLockerAlbumBinding

class AlbumLockerRVAdapter(private var albumList:ArrayList<Album>): RecyclerView.Adapter<AlbumLockerRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onRemoveAlbum(position: Int)
        fun onSwitch(position: Int)
        fun offSwitch(position: Int)
    }
    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener=itemClickListener
    }
    fun removeItem(position: Int){
        albumList.removeAt(position)
        notifyDataSetChanged()
    }
    fun switch(position: Int){
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemLockerAlbumBinding =
            ItemLockerAlbumBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int =albumList.size

    override fun onBindViewHolder(holder: AlbumLockerRVAdapter.ViewHolder, position: Int) {
        holder.bind(albumList[position])
        holder.binding.itemAlbumMoreIv.setOnClickListener{
            mItemClickListener.onRemoveAlbum(position) }
        holder.binding.itemSwitchOffIv.setOnClickListener{
            mItemClickListener.onSwitch(position)
        }
        holder.binding.itemSwitchOnIv.setOnClickListener{
            mItemClickListener.offSwitch(position)
        }
    }

    inner class ViewHolder(val binding: ItemLockerAlbumBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(album:Album){
            binding.itemAlbumTitleTv.text=album.title
            binding.itemAlbumSingerTv.text=album.singer
            binding.itemAlbumImgIv.setImageResource(album.coverImg!!)
            if(album.switchOn == true){
                binding.itemSwitchOffIv.visibility=View.GONE
                binding.itemSwitchOnIv.visibility=View.VISIBLE
            }else{
                binding.itemSwitchOffIv.visibility=View.VISIBLE
                binding.itemSwitchOnIv.visibility=View.GONE
            }
        }
    }

}