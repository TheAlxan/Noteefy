package core

sealed class NoteefyConfig private constructor(){
    object DispatcherConfig : NoteefyConfig() {
        var defaultThreadsCount: Int = 30
    }
}
