package issuing.domain_event

import com.sansan.billone.lib.TraceContext

interface Subscriber {
  fun notify(
    message: String,
    traceContext: TraceContext,
  )
}
