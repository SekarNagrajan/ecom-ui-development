import { links } from '../config/links'

const FOOTER_LINKS = [
  { label: 'Terms and Condition', href: links.terms },
  { label: 'Privacy', href: links.privacy },
  { label: 'Support', href: links.support },
]

export default function SiteFooter() {
  return (
    <footer className="bg-surface-muted py-[29.5px]">
      <div className="shell-narrow flex flex-col gap-3 text-[14px] md:flex-row md:items-center md:justify-between">
        <p className="text-black opacity-60">© 2026 Solverminds Solutions &amp; Technologies Pvt Ltd.</p>
        <nav aria-label="Legal" className="flex flex-wrap gap-[20px]">
          {FOOTER_LINKS.map((link) => (
            <a key={link.label} href={link.href} className="whitespace-nowrap text-brand hover:underline">
              {link.label}
            </a>
          ))}
        </nav>
      </div>
    </footer>
  )
}
