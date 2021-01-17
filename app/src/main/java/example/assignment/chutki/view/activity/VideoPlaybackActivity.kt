package example.assignment.chutki.view.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerView
import dagger.hilt.android.AndroidEntryPoint
import example.assignment.chutki.R
import example.assignment.chutki.extension.UIExtensions.heightAnimator
import example.assignment.chutki.view.adapter.VideoItemCallback
import example.assignment.chutki.view.adapter.VideosAdapter
import example.assignment.chutki.viewmodel.VideoPlaybackActivityViewModel
import kotlinx.android.synthetic.main.activity_video_playback.*
import kotlinx.android.synthetic.main.custom_playback_control.*
import kotlin.properties.Delegates

@AndroidEntryPoint
class VideoPlaybackActivity: BaseActivity<VideoPlaybackActivityViewModel>(true), VideoItemCallback, Player.EventListener {

    companion object {
        const val SELECTED_ITEM_POSITION_KEY = "selectedCategory"
    }

    override val layoutId: Int
        get() = R.layout.activity_video_playback

    override val viewModelType: Class<VideoPlaybackActivityViewModel>
        get() = VideoPlaybackActivityViewModel::class.java


    private lateinit var videoListRecyclerView: RecyclerView
    private lateinit var videoListRecyclerAdapter: VideosAdapter
    private var player: SimpleExoPlayer? = null
    private lateinit var playerView: PlayerView
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private lateinit var progressBar: ProgressBar
    private var videoListRecyclerViewHeight: Float? = null
    private lateinit var exitButton: ImageView
    private lateinit var fullScreenButton: ImageView

    private var isFullScreen by Delegates.observable(false) {
            prop, old, new ->
        animateRecyclerView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fetchIntentParams()
        initUI()
        fetchVideos()
        makeActivityFullScreen()
    }


    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        player?.playWhenReady = true
        player?.playWhenReady = playWhenReady
        player?.seekTo(currentWindow, playbackPosition);
        player?.prepare()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player?.playWhenReady?:false
            playbackPosition = player?.currentPosition?:0
            currentWindow = player?.currentWindowIndex?:0
            player?.playWhenReady = false
        }
    }

    private fun fetchIntentParams() {
        viewModel.categorySelected = intent.getStringExtra(SELECTED_ITEM_POSITION_KEY)
    }

    private fun initUI() {
        videoListRecyclerView = category_videos_recycler_view
        playerView = video_view
        progressBar = playback_progress_dialog
        exitButton = exit_video_playback_button
        fullScreenButton = exo_fullscreen_placeholder

        val trackSelector = DefaultTrackSelector(this)
        trackSelector.setParameters(
            trackSelector.buildUponParameters().setMaxVideoSizeSd())

        player = SimpleExoPlayer.Builder(this)
            .setTrackSelector(trackSelector).build()
        playerView.player = player

        player?.addListener(this)

        fullScreenButton.setOnClickListener {
            isFullScreen = !isFullScreen
            fullScreenButton.setBackgroundResource(if(isFullScreen)
            R.drawable.exo_controls_fullscreen_exit else
                R.drawable.exo_controls_fullscreen_enter)
        }

        exitButton.setOnClickListener {
            finish()
        }
    }

    private fun makeActivityFullScreen() {

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_IMMERSIVE)
        }
        supportActionBar?.hide()
    }

    private fun fetchVideos() {
        viewModel.getVideos(onSuccess, onFailure)
    }

    private var onSuccess:() -> Unit = {
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        videoListRecyclerView.layoutManager = layoutManager
        videoListRecyclerAdapter = VideosAdapter(this, viewModel.videos, this, layoutManager)
        videoListRecyclerView.adapter = videoListRecyclerAdapter

        viewModel.videos.getOrNull(0)?.videoURL?.let{
            player?.setMediaItem(
                MediaItem.Builder()
                    .setUri(it)
                    .setMediaId(viewModel.videoIdList[0])
                    .build())
        }

        for(index in 1 until viewModel.videos.size) {
            viewModel.videos.getOrNull(index)?.videoURL?.let{
                player?.addMediaItem(MediaItem.Builder()
                    .setUri(it)
                    .setMediaId(viewModel.videoIdList[index])
                    .build())
            }
        }

        initializePlayer()
    }

    private var onFailure:() -> Unit = {
       showErrorDialog(getString(R.string.generic_error_string)) {
           finish()
       }
    }

    override fun onItemClick(position: Int) {
        player?.getMediaItemAt(position)?.let{
            player?.prepare()
            player?.seekTo(position, C.TIME_UNSET);
            player?.setPlayWhenReady(true)
        }
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        when (playbackState) {
            ExoPlayer.STATE_IDLE -> {
            }

            ExoPlayer.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            ExoPlayer.STATE_READY -> {
                progressBar.visibility = View.GONE
            }
            ExoPlayer.STATE_ENDED -> {
            }
        }
    }

    override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
        super.onMediaItemTransition(mediaItem, reason)
        for(index in 0 until player?.mediaItemCount!!) {
            if(player?.getMediaItemAt(index)?.mediaId == mediaItem?.mediaId &&
                index!= videoListRecyclerAdapter.selectedVideoPosition) {
                videoListRecyclerAdapter.changeSelection(null, index)
                break
            }
        }
    }

    private fun animateRecyclerView() {
        if(videoListRecyclerViewHeight==null)
            videoListRecyclerViewHeight = videoListRecyclerView.height.toFloat()

        videoListRecyclerViewHeight?.let{
            if(isFullScreen) {
                videoListRecyclerView.heightAnimator(it,
                    0.1f, onAnimationEnd = {
                        videoListRecyclerView.visibility = View.GONE
                    })
            } else {
                videoListRecyclerView.visibility = View.VISIBLE
                videoListRecyclerView.heightAnimator(0.1f,
                    it, onAnimationEnd = {
                        videoListRecyclerAdapter.notifyDataSetChanged()
                    })
            }
        }
    }
}