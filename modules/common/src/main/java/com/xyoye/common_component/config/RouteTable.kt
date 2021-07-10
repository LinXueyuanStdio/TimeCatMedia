package com.xyoye.common_component.config

/**
 * Created by xyoye on 2020/9/21.
 *
 * 路由表
 */
object RouteTable {
    object Anime {
        const val HOST = "Anime"
        const val Search = "$HOST/anime/search"
        const val HomeFragment = "$HOST/anime/home_fragment"
        const val AnimeDetail = "$HOST/anime/anime_detail"
        const val AnimeSeason = "$HOST/anime/anime_season"
        const val AnimeFollow = "$HOST/anime/follow"
        const val AnimeTag = "$HOST/anime/tag"
        const val AnimeHistory = "$HOST/anime/history"
    }

    object Local {
        const val HOST = "Local"
        const val MediaFragment = "$HOST/local/media_fragment"
        const val BindDanmu = "$HOST/local/bind_danmu"
        const val BindSubtitle = "$HOST/local/bind_subtitle"
        const val LocalMediaStorage = "$HOST/local/local_media_storage"
        const val PlayHistory = "$HOST/local/play_history"
        const val BiliBiliDanmu = "$HOST/local/bilibili_danmu"
        const val ShooterSubtitle = "$HOST/local/shooter_subtitle"
    }

    object User {
        const val HOST = "User"
        const val PersonalFragment = "$HOST/user/personal_fragment"
        const val UserLogin = "$HOST/user/login"
        const val UserRegister = "$HOST/user/register"
        const val UserForgot = "$HOST/user/forgot"
        const val UserInfo = "$HOST/user/info"
        const val SettingPlayer = "$HOST/user/setting_player"
        const val SettingApp = "$HOST/user/setting_app"
        const val WebView = "$HOST/user/web_view"
        const val ScanManager = "$HOST/user/scan_manager"
        const val CacheManager = "$HOST/user/cache_manager"
        const val CommonManager = "$HOST/user/common_manager"
        const val Feedback = "$HOST/user/feedback"
        const val AboutUs = "$HOST/user/about_us"
        const val License = "$HOST/user/license"
        const val SwitchTheme = "$HOST/user/switch_theme"
    }

    object Player {
        const val HOST = "Player"
        const val Player = "$HOST/player/player_interceptor"
        const val PlayerCenter = "$HOST/player/player"
    }

    object Download {
        const val HOST = "Download"
        const val DownloadSelection = "$HOST/download/selection"
        const val DownloadList = "$HOST/download/list"
        const val DownloadDetail = "$HOST/download/detail"
        const val PlaySelection = "$HOST/play/selection"
    }

    object Stream {
        const val HOST = "Stream"
        const val WebDavLogin = "$HOST/stream/web_dav_login"
        const val WebDavFile = "$HOST/stream/web_dav_file"
        const val FTPLogin = "$HOST/stream/ftp_login"
        const val FTPFile = "$HOST/stream/ftp_file"
        const val SmbLogin = "$HOST/stream/smb_login"
        const val SmbFile = "$HOST/stream/smb_file"
        const val RemoteLogin = "$HOST/stream/remote_login"
        const val RemoteScan = "$HOST/stream/remote_scan"
        const val RemoteFile = "$HOST/stream/remote_file"
        const val RemoteControl = "$HOST/stream/remote_control"
    }
}