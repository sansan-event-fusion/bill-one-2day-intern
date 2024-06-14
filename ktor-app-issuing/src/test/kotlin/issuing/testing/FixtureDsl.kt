package issuing.testing

@DslMarker
private annotation class FixtureDsl

private typealias TableFixture = Any

@FixtureDsl
interface FixtureContext<TContextFixture : TableFixture> {
  fun <T : TableFixture> insert(fixture: T): T

  private val contextFixture: FixtureContext<TContextFixture> get() = this

  infix fun <T : TableFixture> T.associates(callback: AssociatingContext<T>.(parent: T) -> Unit): T {
    val fixture = this
    AssociatingContext(contextFixture, fixture).callback(fixture)
    return fixture
  }
}

class AssociatingContext<TContextFixture : TableFixture>(
  private val parentContext: FixtureContext<*>,
  val parent: TContextFixture,
) : FixtureContext<TContextFixture> {
  override fun <T : TableFixture> insert(fixture: T): T {
    return parentContext.insert(fixture)
  }
}

private class DummyRootFixture

class RootFixtureContext : FixtureContext<DummyRootFixture> {
  private val mutableFixtures = mutableListOf<TableFixture>()
  val fixtures: List<TableFixture> get() = mutableFixtures.toList()

  override fun <T : TableFixture> insert(fixture: T): T {
    mutableFixtures.add(fixture)
    return fixture
  }
}
