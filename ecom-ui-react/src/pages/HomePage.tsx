import Hero from '../sections/Hero'
import HowItWorks from '../sections/HowItWorks'
import SignupBanner from '../sections/SignupBanner'
import SiteFooter from '../sections/SiteFooter'

export default function HomePage() {
  return (
    <>
      <main>
        <Hero />
        <HowItWorks />
        <SignupBanner />
      </main>
      <SiteFooter />
    </>
  )
}
