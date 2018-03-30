using System;
using Xunit;
using Akka.Streams;
using Akka.Streams.Dsl;
using Akka.Actor;
using System.Collections.Generic;
using Akka;
using Reactive.Streams;
using System.Threading.Tasks;
using Akka.TestKit.Xunit2;

namespace diving_into_akka_streams
{
    public class StreamShould : TestKit
    {
        // Streams are driven by backpressure. It allows to avoid crashing
        // streams because of faster produce than consumers.
        [Fact]
        public void UseBackpressure()
        {
            using (var materializer = Sys.Materializer())
            {
                GraphDsl.Create()
            }

        }

        //[Fact]
        public void BroadcastSource()
        {
            var countItems = Sink.Aggregate<int, int>(0, (agg, i) => agg + 1);
            var sumItems = Sink.Aggregate<int, int>(0, (agg, i) => agg + i);

            using (var system = ActorSystem.Create("system"))
            using (var materializer = system.Materializer())
            {
                var items = new List<int> { 1, 2, 3};
                var g = GraphDsl.Create(countItems, sumItems, Keep.Both, (b, ci, si) => {
                    var broadcast = b.Add(new Broadcast<int>(2));
                    var source = Source.From(items);
                    //b.From(source).To(broadcast.In);
                    b.From(broadcast.Out(0))
                     .To(countItems);
                    b.From(broadcast.Out(1))
                     .Via(Flow.Create<int>().Select(it => it * 2))
                     .To(sumItems);
                    return ClosedShape.Instance;
                });
                var rg = RunnableGraph.FromGraph(g);

                rg.Run(materializer);
            }
        }
    }
}
