import heroPhoto from '../assets/images/hero-containers.jpg'
import ScheduleSearchCard from './ScheduleSearchCard'
import SiteHeader from './SiteHeader'

// Gradient overlay from the hero image layer (9:468) in Figma. Figma blends gradient stops
// without premultiplying alpha while CSS premultiplies, so the fade into transparency is
// sampled into extra stops to keep the same blue tint.
const heroOverlay = `linear-gradient(132.42deg, ${[
  'rgb(4 18 99) 8.21%',
  'rgb(6 27 145 / 0.8) 45.69%',
  'rgb(6.5 28.8 154.3 / 0.667) 58.63%',
  'rgb(7 30.7 163.7 / 0.533) 71.56%',
  'rgb(7.5 32.5 173 / 0.4) 84.5%',
  'rgb(8 34.3 182.3 / 0.267) 97.43%',
  'rgb(8.5 36.2 191.7 / 0.133) 110.37%',
  'rgb(9 38 201 / 0) 123.3%',
].join(', ')})`

export default function Hero() {
  return (
    <section className="relative isolate overflow-hidden pb-16 lg:h-[965px] lg:pb-0">
      <div aria-hidden="true" className="absolute inset-0 -z-10">
        <img
          src={heroPhoto}
          alt=""
          fetchPriority="high"
          className="size-full object-cover object-top"
        />
        <div className="absolute inset-0" style={{ backgroundImage: heroOverlay }} />
      </div>

      <SiteHeader />

      <div className="shell-wide mt-12 flex flex-col gap-[34px] lg:absolute lg:top-1/2 lg:left-1/2 lg:mt-0 lg:-translate-x-1/2 lg:-translate-y-1/2">
        <div className="text-white">
          <h1 className="text-[40px] leading-[1.5] font-normal md:text-[60px] xl:text-[80px]">
            One Platform, Global Shipping
          </h1>
          {/* The subtitle overlaps the heading's line box by 8px in the design. */}
          <p className="text-[18px] leading-[1.5] md:text-[20px] xl:-mt-[8px]">
            Search routes, track shipments, and check rates.
          </p>
        </div>
        <ScheduleSearchCard />
      </div>
    </section>
  )
}
