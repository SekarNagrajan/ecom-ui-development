import ActionButton from '../components/ActionButton'
import { links } from '../config/links'

export default function SignupBanner() {
  return (
    <section className="bg-navy-deep py-[60px]">
      <div className="shell-narrow flex flex-col gap-[40px] lg:flex-row lg:items-center">
        <div className="flex min-w-0 flex-1 flex-col justify-center gap-[6px] lg:max-w-[910px]">
          <p className="text-[14px] font-extrabold text-accent-sky">NEW TO SAMSARA?</p>
          <h2 className="text-[24px] font-medium text-white md:text-[30px]">
            Search first. Create your shipping account when you need it.
          </h2>
          <p className="text-[18px] font-light text-white opacity-80 md:text-[20px]">
            Registration unlocks booking and the authenticated customer workspace.
          </p>
        </div>
        <div className="flex shrink-0 gap-[17px]">
          <ActionButton variant="solid-light" href={links.register}>
            Register
          </ActionButton>
          <ActionButton variant="outline-light" href={links.login} className="w-[122px]">
            Login
          </ActionButton>
        </div>
      </div>
    </section>
  )
}
