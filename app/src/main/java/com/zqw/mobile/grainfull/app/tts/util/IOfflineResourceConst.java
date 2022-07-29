package com.zqw.mobile.grainfull.app.tts.util;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.TtsMode;

public interface IOfflineResourceConst {

    // OFFLINE 代表 离线合成，ONLINE 代表 在线合成。
    TtsMode DEFAULT_SDK_TTS_MODE = TtsMode.ONLINE;

    /** 下面参数 纯离线SDK版本才有 */

    String VOICE_FEMALE = "F";

    String VOICE_MALE = "M";

    String VOICE_DUYY = "Y";

    String VOICE_DUXY = "X";

    String TEXT_MODEL = "bd_etts_common_text_txt_all_mand_eng_middle_big_v4.1.0_20211223.dat";

    String VOICE_MALE_MODEL = "bd_etts_navi_speech_m15_mand_eng_high_am-style24k_v4.6.0_20210721.dat";

    String VOICE_FEMALE_MODEL = "bd_etts_navi_speech_f7_mand_eng_high_am-style24k_v4.6.0_20210721.dat";

    String VOICE_DUXY_MODEL = "bd_etts_navi_speech_yy_mand_eng_high_am-style24k_v4.6.0_20210721.dat";

    String VOICE_DUYY_MODEL = "bd_etts_navi_speech_c1_mand_eng_high_am-style24k_v4.6.0_20210721.dat";

    String PARAM_SN_NAME = SpeechSynthesizer.PARAM_AUTH_SN;
}
