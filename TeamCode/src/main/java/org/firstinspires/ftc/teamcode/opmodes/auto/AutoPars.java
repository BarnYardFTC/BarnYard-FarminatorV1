package org.firstinspires.ftc.teamcode.opmodes.auto;

public enum AutoPars {
    ;

    public enum side {
        RED,
        BLUE,
        CAMERA_BASED
    }

    public enum posDistance {
        CLOSE,
        FAR
    }

    public enum positions {
        START_CLOSE,
        START_FAR,
        SHOOT_CLOSE,
        SHOOT_FAR,
        LEFT_COLLECT,
        MID_COLLECT,
        FAR_COLLECT,
        LEFT_READY_COLLECT,
        MID_READY_COLLECT,
        FAR_READY_COLLECT,
        LOAD_COLLECT,
        LEFT_LOAD_COLLECT,
        MID_LOAD_COLLECT,
        FAR_LOAD_COLLECT,
        GATE_OPEN,
        GATE_COLLECT,
        PARK,
        LEFT_SHOOT,
        MID_SHOOT,
        FAR_SHOOT,
        SHOOT_NUDGE
    }

    public enum headings {
        SHOOT,
        LEFT,
        RIGHT
    }



}