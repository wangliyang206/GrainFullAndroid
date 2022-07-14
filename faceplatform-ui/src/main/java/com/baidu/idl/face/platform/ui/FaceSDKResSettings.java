/**
 * Copyright (C) 2017 Baidu Inc. All rights reserved.
 */
package com.baidu.idl.face.platform.ui;

import com.baidu.idl.face.platform.FaceEnvironment;
import com.baidu.idl.face.platform.FaceStatusNewEnum;

/**
 * sdk使用Res资源设置功能
 */
public class FaceSDKResSettings {

    /**
     * 初始化Id（新）
     */
    public static void initializeResId() {
        // Sound Res Id
        FaceEnvironment.setSoundId(FaceStatusNewEnum.DetectRemindCodeNoFaceDetected,
                R.raw.detect_face_in);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.DetectRemindCodeBeyondPreviewFrame,
                R.raw.detect_face_in);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.DetectRemindCodeNoFaceDetected,
                R.raw.detect_face_in);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLiveEye,
                R.raw.liveness_eye);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLiveMouth,
                R.raw.liveness_mouth);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLivePitchUp,
                R.raw.liveness_head_up);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLivePitchDown,
                R.raw.liveness_head_down);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLiveYawLeft,
                R.raw.liveness_head_left);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionTypeLiveYawRight,
                R.raw.liveness_head_right);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.FaceLivenessActionComplete,
                R.raw.face_good);
        FaceEnvironment.setSoundId(FaceStatusNewEnum.OK, R.raw.face_good);

        // Tips Res Id
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeNoFaceDetected,
                R.string.detect_face_in);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeBeyondPreviewFrame,
                R.string.detect_face_in);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodePoorIllumination,
                R.string.detect_low_light);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeImageBlured,
                R.string.detect_keep);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionLeftEye,
                R.string.detect_occ_left_eye);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionRightEye,
                R.string.detect_occ_right_eye);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionNose,
                R.string.detect_occ_nose);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionMouth,
                R.string.detect_occ_mouth);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionLeftContour,
                R.string.detect_occ_left_check);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionRightContour,
                R.string.detect_occ_right_check);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeOcclusionChinContour,
                R.string.detect_occ_chin);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodePitchOutofUpRange,
                R.string.detect_head_down);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodePitchOutofDownRange,
                R.string.detect_head_up);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeYawOutofLeftRange,
                R.string.detect_head_right);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeYawOutofRightRange,
                R.string.detect_head_left);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeTooFar,
                R.string.detect_zoom_in);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeTooClose,
                R.string.detect_zoom_out);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeLeftEyeClosed,
                R.string.detect_left_eye_close);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeRightEyeClosed,
                R.string.detect_right_eye_close);

        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLiveEye,
                R.string.liveness_eye);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLiveMouth,
                R.string.liveness_mouth);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLivePitchUp,
                R.string.liveness_head_up);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLivePitchDown,
                R.string.liveness_head_down);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLiveYawLeft,
                R.string.liveness_head_left);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionTypeLiveYawRight,
                R.string.liveness_head_right);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.FaceLivenessActionComplete,
                R.string.liveness_good);
        FaceEnvironment.setTipsId(FaceStatusNewEnum.OK, R.string.liveness_good);

        FaceEnvironment.setTipsId(FaceStatusNewEnum.DetectRemindCodeTimeout,
                R.string.detect_timeout);
    }
}
