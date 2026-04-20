package hoodles.morphe.patches.podcastaddict.misc.gms

import hoodles.morphe.patches.podcastaddict.shared.Constants
import hoodles.morphe.patches.shared.misc.gms.gmsCoreSupportPatch

@Suppress("unused")
val gmsCoreSupportPatch = gmsCoreSupportPatch(
    mainActivityName = "/PodcastListActivity;",
    spoofedPackageSignature = "751a69848dc3bc374ee20bf705cab6fb641e6ac1",
) {
    compatibleWith(Constants.COMPATIBILITY)
}