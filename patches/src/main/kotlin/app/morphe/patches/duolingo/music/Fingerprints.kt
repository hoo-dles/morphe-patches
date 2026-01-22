package app.morphe.patches.duolingo.music

import app.morphe.patcher.Fingerprint

// Matches toString()
object MusicCourseFingerprint : Fingerprint (
    strings = listOf("Music(courseSummary=", ", activePathSectionId=")
)