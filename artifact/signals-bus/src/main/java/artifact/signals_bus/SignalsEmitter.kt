package artifact.signals_bus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import artifact.signals_bus.contract.Navigational
import artifact.signals_bus.contract.Signal
import java.util.*

class SignalsEmitter<SignalsParent : Signal>(private val signature: String) {
    private val signalsQueue = LinkedList<SignalsParent>()

    private var isWaitingForAcknowledgement: Boolean = false

    private val _emitter = MutableLiveData<SignalsParent>()
    val emitter: LiveData<SignalsParent> = _emitter

    fun acknowledgeSignal(signal: SignalsParent) {
        if (signal is Navigational) {
            isWaitingForAcknowledgement = false
            signalsQueue.clear()
            _emitter.value = null
        } else if (signal.signature == signature)
            pollNext()
    }

    fun enqueueSignal(signal: SignalsParent) {
        signal.signature = signature
        signalsQueue.add(signal)
        if (!isWaitingForAcknowledgement) {
            isWaitingForAcknowledgement = true
            pollNext()
        }
    }

    private fun pollNext() {
        if (signalsQueue.isEmpty()) {
            isWaitingForAcknowledgement = false
            _emitter.value = null
        } else
            _emitter.postValue(signalsQueue.poll())
    }
}