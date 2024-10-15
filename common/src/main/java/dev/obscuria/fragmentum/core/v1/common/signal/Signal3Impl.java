package dev.obscuria.fragmentum.core.v1.common.signal;

import dev.obscuria.fragmentum.api.v1.common.signal.Signal3;

public final class Signal3Impl<P1, P2, P3> extends SignalImpl<Signal3.Listener<P1, P2, P3>> implements Signal3<P1, P2, P3>
{
    public void emit(P1 p1, P2 p2, P3 p3)
    {
        this.emit(listener -> listener.consume(p1, p2, p3));
    }
}