using System;
using System.IO;
using System.Linq;
using NUnit.Framework;
using Xamarin.UITest;
using Xamarin.UITest.Queries;

namespace WorkspaceOne.Example.UITests
{
    [TestFixture(Platform.iOS)]
    [TestFixture(Platform.Android)]
    public class Tests
    {
        IApp app;
        Platform platform;

        public Tests(Platform platform)
        {
            this.platform = platform;
        }

        [SetUp]
        public void BeforeEachTest()
        {
            app = AppInitializer.StartApp(platform);
        }

        [Test]
        public void AppLaunches()
        {
            app.Screenshot("First screen.");
        }

        [Test]
        public void AppDoesntCrashAfterTime() // VXF-45, VXF-46
        {
            app.WaitForNoElement("no element", timeout: TimeSpan.FromMinutes(3.0));
            app.Screenshot("No Crash");
        }
    }
}
