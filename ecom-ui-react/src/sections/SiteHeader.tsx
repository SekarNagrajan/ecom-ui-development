import logo from '../assets/images/svm-logo@2x.png'
import ActionButton from '../components/ActionButton'
import Icon from '../components/Icon'
import { links } from '../config/links'

function Divider() {
  return <Icon name="divider" className="hidden md:block" />
}

export default function SiteHeader() {
  return (
    <header className="shell-header relative flex flex-wrap items-center justify-between gap-4 py-[30px]">
      <a href="/" className="shrink-0" aria-label="Solverminds home">
        <img
          src={logo}
          alt="Solverminds Solutions and Technologies"
          width={186}
          height={54}
          className="h-[54px] w-[186px] object-cover"
        />
      </a>

      <nav aria-label="Account" className="flex flex-wrap items-center gap-[16px]">
        <ActionButton variant="outline-light" icon="user" href={links.login}>
          Login
        </ActionButton>
        <ActionButton variant="outline-light" icon="signUp" href={links.register}>
          Register
        </ActionButton>
        <Divider />
        <button
          type="button"
          aria-label="Change language"
          className="relative size-[24px] shrink-0 cursor-pointer rounded-sm focus-visible:outline-2 focus-visible:outline-white"
        >
          <Icon name="translate" className="absolute top-[3.5px] left-[3px]" />
        </button>
        <Divider />
        <ActionButton variant="solid-brand" icon="customerSupport" href={links.contact}>
          Contact Us
        </ActionButton>
      </nav>
    </header>
  )
}
