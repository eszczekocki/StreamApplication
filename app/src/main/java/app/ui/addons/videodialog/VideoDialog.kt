package app.ui.addons.videodialog


import android.app.Dialog
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class VideoDialog() : DialogFragment() {
    private var lastOrientation: Int? = null
    private var fullScreenDialog: Dialog?=null
    private lateinit var player: ExoPlayer
    private var videoUri: String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        videoUri = arguments?.getString("uri")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(com.example.streamingapp.R.layout.fragment_video_dialog, container, false)
        var contentView = view?.findViewById<FrameLayout>(com.example.streamingapp.R.id.enclosingLayout);
        var playerView = view?.findViewById<PlayerView>(com.example.streamingapp.R.id.mediaPlayer)
        player = ExoPlayer.Builder(requireContext()).build()
        playerView?.player = player
        val mediaItem = videoUri?.let { MediaItem.fromUri(it) }
        if (mediaItem != null) {
            player.setMediaItem(mediaItem)
        }
        player.prepare()
        player.playWhenReady = true
        playerView?.setFullscreenButtonClickListener {
            if(it) {
                lastOrientation = this.resources.configuration.orientation
                if (lastOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
                fullScreenDialog =
                    Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
                (playerView.parent as ViewGroup).removeView(playerView)
                fullScreenDialog?.addContentView(
                    playerView,
                    ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                )
                fullScreenDialog?.show()
            } else {
                (playerView.parent as ViewGroup).removeView(playerView)
                (contentView as FrameLayout).addView(playerView)
                fullScreenDialog?.let{
                    it.dismiss()
                }
                 lastOrientation?.let {
                     activity?.requestedOrientation = it
                }
            }
        }
        return view
    }
}
