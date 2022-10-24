package artifact.signals_bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import artifact.signals_bus.contract.Emitter
import artifact.signals_bus.contract.Signal
import java.util.*

class SignalsReceiver<SignalsParent : Signal>(private val lifecycleOwner: LifecycleOwner) {
    private val signalsQueue = LinkedList<SignalsParent>()

    private val _internalEmitter = MutableLiveData<SignalsParent>()
    private val internalEmitter: LiveData<SignalsParent> = _internalEmitter

    private var emitters: List<Emitter<SignalsParent>>? = null

    private var signalsHandler: ((SignalsParent) -> Unit)? = null

    private val acknowledgementHandler: (SignalsParent) -> Unit = { signal ->
        emitters?.forEach { emitter ->
            emitter.acknowledgeSignal(signal)
        }
    }

    private val signalsObserver = Observer<SignalsParent> { signal ->
        signal?.let {
            signalsHandler?.invoke(it)
            acknowledgementHandler.invoke(it)
            resume()
        } ?: resume()
    }

    private var isProcessing: Boolean = false

    private val externalSignalsObserver = Observer<SignalsParent> { signal ->
        signalsQueue.add(signal)
        if (!isProcessing) {
            isProcessing = true
            resume()
        }
    }

    private fun resume() {
        if (signalsQueue.isEmpty())
            isProcessing = false
        else
            _internalEmitter.postValue(signalsQueue.poll())
    }

    fun initializeSignalsReceiver(
        signalsHandler: ((SignalsParent) -> Unit),
        vararg emitters: Emitter<SignalsParent>
    ) {
        emitters.forEach { emitter ->
            emitter.signalsEmitter.emitter.observe(lifecycleOwner, externalSignalsObserver)
        }
        this.emitters = emitters.toList()
        this.signalsHandler = signalsHandler
        internalEmitter.observe(lifecycleOwner, signalsObserver)
    }
}