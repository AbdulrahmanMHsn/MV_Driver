package artifact.signals_bus.contract

import artifact.signals_bus.SignalsEmitter


interface Emitter<SignalsParent : Signal> {
    val signalsEmitter: SignalsEmitter<SignalsParent>

    fun acknowledgeSignal(signal: SignalsParent) {
        signalsEmitter.acknowledgeSignal(signal)
    }

    fun enqueueSignal(vararg signals: SignalsParent) {
        signals.forEach {
            signalsEmitter.enqueueSignal(it)
        }
    }
}