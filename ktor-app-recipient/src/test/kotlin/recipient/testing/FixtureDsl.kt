package recipient.testing

@DslMarker
private annotation class FixtureDsl

private typealias TableFixture = Any

@FixtureDsl
interface IFixtureContext<TContextFixture : TableFixture> {
    fun <T : TableFixture> insert(fixture: T): T

    private val contextFixture: IFixtureContext<TContextFixture> get() = this

    infix fun <T : TableFixture> T.associates(callback: AssociatingContext<T>.(parent: T) -> Unit): T {
        val fixture = this
        AssociatingContext(contextFixture, fixture).callback(fixture)
        return fixture
    }
}

class AssociatingContext<TContextFixture : TableFixture>(
    private val parentContext: IFixtureContext<*>,
    val parent: TContextFixture,
) : IFixtureContext<TContextFixture> {
    override fun <T : TableFixture> insert(fixture: T): T {
        return parentContext.insert(fixture)
    }
}

private class DummyRootFixture

class RootFixtureContext : IFixtureContext<DummyRootFixture> {
    private val mutableFixtures = mutableListOf<TableFixture>()
    val fixtures: List<TableFixture> get() = mutableFixtures.toList()

    override fun <T : TableFixture> insert(fixture: T): T {
        mutableFixtures.add(fixture)
        return fixture
    }
}
