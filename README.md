<div align="center"> 
<picture>
    <source
      width="512px"
      media="(prefers-color-scheme: dark)"
      srcset="https://raw.githubusercontent.com/MorpheApp/.github/refs/heads/main/profile/assets/morphe-wordmark/morphe_wordmark_dark.svg"
    />
    <img 
      width="512px"
      src="https://raw.githubusercontent.com/MorpheApp/.github/refs/heads/main/profile/assets/morphe-wordmark/morphe_wordmark_light.svg"
    />
</picture>
</div>

# :leaves: hoodles Morphe Patches

![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/hoo-dles/morphe-patches/release.yml)
![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)

<br/>

> [!NOTE]
> Patch requests are always welcome, so feel free to open an issue.
>
> If you find this project useful and want to help support its development, consider [donating](.donate/donate.md). :pray:

<br/>

| App                            | Package                            | Patches                                                                                               |
|--------------------------------|------------------------------------|-------------------------------------------------------------------------------------------------------|
| All-In-One Calculator          | `all.in.one.calculator`            | <ul><li>Disable ads</li><ul>                                                                          |
| Amazon Prime Video             | `com.amazon.avod.thirdpartyclient` | <ul><li>Skip ads</li></ul>                                                                            |
| Avocards                       | `com.avocards`                     | <ul><li>Enable Premium</li></ul>                                                                      |
| Busuu                          | `com.busuu.android.enc`            | <ul><li>Enable Premium</li></ul>                                                                      |
| Cake :dart:                    | `me.mycake`                        | <ul><li>Enable Plus</li></ul>                                                                         |
| DAILY POCKET :earth_americas:  | `kr.co.yjteam.dailypay`            | <ul><li>Enable Premium</li></ul>                                                                      |
| Duolingo                       | `com.duolingo`                     | <ul><li>Disable Play Integrity check</li><li>Enable debug mode</li><li>Enable Super/MAX</li></ul>     |
| Eggbun                         | `kr.eggbun.eggconvo`               | <ul><li>Enable Premium</li><li>Force native keyboard</li></ul>                                        |
| FotMob                         | `com.mobilefootie.wc2010`          | <ul><li>Enable FotMob+</li></ul>                                                                      |
| HelloChinese :dart: :computer: | `com.hellochinese`                 | <ul><li>Enable Premium</li></ul>                                                                      |
| IbisPaint X :dart: :computer:  | `jp.ne.ibis.ibispaintx.app`        | <ul><li>Enable Prime Membership</li></ul>                                                             |
| Icon Packer                    | `cn.ommiao.iconpacker`             | <ul><li>Unlocked premium features</li></ul>                                                           |
| Lingory :dart: :computer:      | `org.languageapp.lingory`          | <ul><li>Enable Premium</li></ul>                                                                      |
| Meme Generator                 | `com.zombodroid.MemeGenerator`     | <ul><li>Enable Pro</li></ul>                                                                          |
| Merriam-Webster                | `com.merriamwebster`               | <ul><li>Enable Premium</li></ul>                                                                      |
| Mimo                           | `com.getmimo`                      | <ul><li>Enable Pro</li></ul>                                                                          |
| MyExpenses                     | `org.totschnig.myexpenses`         | <ul><li>Enable Pro</li></ul>                                                                          |
| MyFitnessPal                   | `com.myfitnesspal.android`         | <ul><li>Enable Premium+</li></ul>                                                                     |
| NOMone Desktop                 | `nom.vrd`                          | <ul><li>Disable anti-tamper</li><li>Disable telemetry</li><li>Remove trial limit</li></ul>            |
| Pandora                        | `com.pandora.android`              | <ul><li>Disable ads</li><li>Unlimited skips</li></ul>                                                 |
| Podcast Addict                 | `com.bambuna.podcastaddict`        | <ul><li>Enable Premium</li></ul>                                                                      |
| Proton VPN                     | `ch.protonvpn.android`             | <ul><li>Remove server change delay</li><li>Unlock custom DNS</li><li>Unlock split tunneling</li></ul> |
| Smart Launcher                 | `ginlemon.flowerfree`              | <ul><li>Enable Pro</li></ul>                                                                          |
| Sofascore                      | `com.sofascore.results`            | <ul><li>Disable ads</li></ul>                                                                         |
| Solid Explorer                 | `pl.solidexplorer2`                | <ul><li>Enable Pro</li><ul>                                                                           |
| Teuida :earth_americas:        | `net.teuida.teuida`                | <ul><li>Enable Premium</li></ul>                                                                      |
| TTMIK Stories :dart:           | `app.ttmikstories.android`         | <ul><li>Enable Premium</li></ul>                                                                      |
| Wallcraft                      | `com.wallpaperscraft.wallpaper`    | <ul><li>Enable Premium</li></ul>                                                                      |
| Windy                          | `com.windyty.android`              | <ul><li>Enable Premium</li></ul>                                                                      |
| World Map Quiz                 | `com.qbis.guessthecountry`         | <ul><li>Enable Premium</li></ul>                                                                      |
| WPS Office                     | `cn.wps.moffice_eng`               | <ul><li>Disable anti-tamper checks</li><li>Enable Pro</li></ul>                                       |

:earth_americas: _Supports MicroG integration._\
:dart: _This app has a strict requirement to use the version defined in the patch._\
:computer: _These patches will only work for devices running `arm64-v8a` CPUs._

<br>

## Frequently Asked Questions :man_shrugging:

#### How do I use this thing?
Add this repo (https://github.com/hoo-dles/morphe-patches) as a remote patch source to Morphe Manager.

#### Help, why can't I log in with my Google account? Why doesn't Google Drive work?
MicroG integration is needed for features that require Google Play Services. Apps that are currently supported are marked with a :earth_americas:.

#### What apk version should I use?
Versions I have tested are defined in each patch, but they may (hopefully?) work for newer app releases. Give it a shot and open a ticket if there's issues. Patches marked with a :dart: will, almost certainly, only work with that specific version.

#### Can you make a patch for \[Super Cool App Goes Here\]?
Maybe? Each patch is different, and many features use server-side functionality that cannot be modified. Before submitting a request, please check the existing open (and closed!) issues to reduce duplication.

#### You haven't completed my patch request.. Did you not see it? / Did you forget? / Why do you hate me?
I did see it. I haven't forgotten. We're good, bro. I'm a one-person "team" who does this stuff in my free time. If it's within my technical capabilities, I'll get around to it at some point.
